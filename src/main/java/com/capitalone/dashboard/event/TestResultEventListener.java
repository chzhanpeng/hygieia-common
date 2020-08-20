package com.capitalone.dashboard.event;

import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Performance;
import com.capitalone.dashboard.model.PerformanceType;
import com.capitalone.dashboard.model.TestCapability;
import com.capitalone.dashboard.model.TestCase;
import com.capitalone.dashboard.model.TestCaseStatus;
import com.capitalone.dashboard.model.TestResult;
import com.capitalone.dashboard.model.TestSuiteType;
import com.capitalone.dashboard.repository.CollectorItemRepository;
import com.capitalone.dashboard.repository.CollectorRepository;
import com.capitalone.dashboard.repository.PerformanceRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class TestResultEventListener extends AbstractMongoEventListener<TestResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestResultEventListener.class);

    private static final String STR_RESP_TIME_THRESHOLD = "KPI : Avg response times";
    private static final String STR_ACTUAL_RESP_TIME = "Actual Response Time";
    private static final String STR_TARGET_RESP_TIME = "Target Response Time";
    private static final String STR_TXN_PER_SEC_THRESHOLD = "KPI : Transaction Per Second";
    private static final String STR_ACTUAL_TXN_PER_SEC = "Actual Transactions per sec";
    private static final String STR_TARGET_TXN_PER_SEC = "Target Transactions per sec";
    private static final String STR_ERROR_RATE_THRESHOLD = "KPI : Error Rate Threshold";
    private static final String STR_ACTUAL_ERROR_RATE = "Actual Error Rate";
    private static final String STR_TARGET_ERROR_RATE = "Target Error Rate";
    private static final String STR_CRITICAL = "CRITICAL";
    private static final String STR_OPEN = "OPEN";
    private static final String COLLECTOR_NAME = "PerfTools";
    private static final String KEY_JOB_NAME = "jobName";
    private static final int SIXTY_SECS = 60;
    private static double HEALTH_GOOD = 1.0;
    // 1.0 is good health, value less than 1.0 considered bad
    private static double HEALTH_CRITICAL = 0.98;
    private static long SEVERITY_CRITICAL = 2;
    private static long SEVERITY_GOOD = 0;

    private double targetRespTime;
    private double actualRespTime;
    private double targetTxnsPerSec;
    private double actualTxnsPerSec;
    private double actualErrorRate;
    private double targetErrorRate;
    private double actualErrorsVal;
    private double txnHealthPercentVal;
    private long errRateSeverityVal;
    private long respTimeSeverityVal;
    private boolean isResponseTimeGood = false;
    private boolean isTxnGoodHealth = false;
    private boolean isErrorRateGood = false;

    private final PerformanceRepository performanceRepository;
    private final CollectorRepository collectorRepository;
    private final CollectorItemRepository collectorItemRepository;

    private enum PERFORMANCE_METRICS {
        AVERAGE_RESPONSE_TIME,
        TOTAL_CALLS,
        ERRORS_PER_MIN,
        ACTUAL_ERROR_RATE,
        BUSINESS_TRANSACTION_HEALTH_PERCENT,
        NODE_HEALTH_PERCENT,
        VIOLATION_OBJECT,
        TOTAL_ERRORS,
        ERROR_RATE_SEVERITY,
        RESPONSE_TIME_SEVERITY,
        CALLS_PER_MIN,
        TARGET_RESPONSE_TIME,
        TARGET_TRANSACTION_PER_SEC,
        TARGET_ERROR_RATE_THRESHOLD
    }

    private enum VIOLATION_ATTRIBUTES {
        SEVERITY,
        INCIDENT_STATUS
    }

    @Autowired
    public TestResultEventListener(CollectorRepository collectorRepository, CollectorItemRepository collectorItemRepository,
                                   PerformanceRepository performanceRepository) {
        this.collectorRepository = collectorRepository;
        this.collectorItemRepository = collectorItemRepository;
        this.performanceRepository = performanceRepository;
    }

    /**
     * Create and save performance object for every test_result type is performance
     * @param event
     */
    @Override
    public void onAfterSave(AfterSaveEvent<TestResult> event) {
        TestResult testResult = event.getSource();
        LOGGER.info("TestResult save event triggered");

        // Ignore anything other than performance tests
        if (!TestSuiteType.Performance.equals(testResult.getType())) {
            LOGGER.info("TestResult save event ignored since its not performance type");
            return;
        }

        List<TestCapability> testCapabilities = new ArrayList<>(testResult.getTestCapabilities());
        if (CollectionUtils.isEmpty(testCapabilities)){
            LOGGER.info("No Test Capabilities found for the test result : {}", testResult.getId());
            return;
        }
        testCapabilities.sort(Comparator.comparing(TestCapability::getTimestamp).reversed());
        TestCapability lastExecutedTestCapabiblity = testCapabilities.iterator().next();
        lastExecutedTestCapabiblity.getTestSuites().forEach(testSuite -> testSuite.getTestCases().forEach(testCase ->
                extractPerformanceMetrics(testCase, lastExecutedTestCapabiblity)));
        CollectorItem perfCollectorItem = getPerfCollectorItem(testResult);
        createPerformanceDoc(testResult, lastExecutedTestCapabiblity, perfCollectorItem);
        LOGGER.info("New performance document created from test_result successfully");
        }

    /**
     * Extracts the test result threshold values to build performance metrics object
     * @param testCase, testCapability
     */
    private void extractPerformanceMetrics(TestCase testCase, TestCapability testCapability) {

        long testCapabilityDurationSecs = TimeUnit.MILLISECONDS.toSeconds(testCapability.getDuration());
        if(testCase.getDescription().equalsIgnoreCase(STR_RESP_TIME_THRESHOLD)){
            isResponseTimeGood = testCase.getStatus().name().equalsIgnoreCase(TestCaseStatus.Success.name());
            testCase.getTestSteps().forEach(testCaseStep -> {
                if (testCaseStep.getId().equalsIgnoreCase(STR_TARGET_RESP_TIME)){
                    targetRespTime = NumberUtils.isCreatable(testCaseStep.getDescription()) ?
                            Double.valueOf(testCaseStep.getDescription()) : NumberUtils.DOUBLE_ZERO;
                }
                if (testCaseStep.getId().equalsIgnoreCase(STR_ACTUAL_RESP_TIME)){
                    actualRespTime = NumberUtils.isCreatable(testCaseStep.getDescription()) ?
                            Double.valueOf(testCaseStep.getDescription()) : NumberUtils.DOUBLE_ZERO;
                }
            });
        }
        if(testCase.getDescription().equalsIgnoreCase(STR_TXN_PER_SEC_THRESHOLD)){
            isTxnGoodHealth = testCase.getStatus().name().equalsIgnoreCase(TestCaseStatus.Success.name());
            testCase.getTestSteps().forEach(testCaseStep -> {
                if (testCaseStep.getId().equalsIgnoreCase(STR_TARGET_TXN_PER_SEC)) {
                    targetTxnsPerSec = NumberUtils.isCreatable(testCaseStep.getDescription()) ?
                            Double.valueOf(testCaseStep.getDescription()) : NumberUtils.DOUBLE_ZERO;
                }
                if (testCaseStep.getId().equalsIgnoreCase(STR_ACTUAL_TXN_PER_SEC)) {
                    actualTxnsPerSec = NumberUtils.isCreatable(testCaseStep.getDescription()) ?
                            Double.valueOf(testCaseStep.getDescription()) : NumberUtils.DOUBLE_ZERO;
                }
            });
        }
        if(testCase.getDescription().equalsIgnoreCase(STR_ERROR_RATE_THRESHOLD)){
            isErrorRateGood = testCase.getStatus().name().equalsIgnoreCase(TestCaseStatus.Success.name());
            testCase.getTestSteps().forEach(testCaseStep -> {
                if (testCaseStep.getId().contains(STR_TARGET_ERROR_RATE)){
                    targetErrorRate = NumberUtils.isCreatable(testCaseStep.getDescription()) ?
                            Double.valueOf(testCaseStep.getDescription()) : NumberUtils.DOUBLE_ZERO;
                }
                if (testCaseStep.getId().equalsIgnoreCase(STR_ACTUAL_ERROR_RATE)){
                    actualErrorRate = NumberUtils.isCreatable(testCaseStep.getDescription()) ?
                            Double.valueOf(testCaseStep.getDescription()) : NumberUtils.DOUBLE_ZERO;
                    // Error rate is percentage of actual errors in total calls
                    actualErrorsVal =  (actualErrorRate / 100) * (testCapabilityDurationSecs * actualTxnsPerSec);
                }
            });
        }
    }

    /**
     * Creates new performance object from test result
     * @param testResult, testCapability, perfCollectorItem
     */
    public Performance createPerformanceDoc(TestResult testResult, TestCapability testCapability, CollectorItem perfCollectorItem) {
        Performance performance = new Performance();
        performance.setId(ObjectId.get());
        performance.setCollectorItemId(perfCollectorItem.getId());
        performance.setType(PerformanceType.ApplicationPerformance);
        performance.setUrl(testResult.getUrl());
        performance.setTimestamp(System.currentTimeMillis());
        performance.setTargetAppName(testResult.getTargetAppName());
        performance.setTargetEnvName(testResult.getTargetEnvName());
        performance.setMetrics(getPerfMetrics(testCapability));
        return performanceRepository.save(performance);
    }
    /**
     * Get performance collector item or create new if not exists already
     * @param testResult
     */
    public CollectorItem getPerfCollectorItem(TestResult testResult) {

        CollectorItem testResultCollItem = collectorItemRepository.findOne(testResult.getCollectorItemId());
        String description = testResultCollItem.getDescription();
        String niceName = testResultCollItem.getNiceName();
        Optional<Map<String, Object>> optOptions = Optional.ofNullable(testResultCollItem.getOptions());
        Optional<Object> optJobName = Optional.ofNullable(optOptions.get().get(KEY_JOB_NAME));
        String jobName = optJobName.isPresent() ? optJobName.get().toString() : "";
        LOGGER.info("Posted Test Result Description(niceName : jobName) - {} : {}", niceName, jobName);
        Collector perfToolsCollector = getPerfToolsCollector();
        Optional<CollectorItem> optCollectorItem = Optional.ofNullable(collectorItemRepository.findByCollectorIdNiceNameAndJobName(
                perfToolsCollector.getId(), niceName, jobName));
        optCollectorItem.ifPresent(collectorItem -> collectorItem.setLastUpdated(System.currentTimeMillis()));
        optCollectorItem = Optional.ofNullable(optCollectorItem.orElseGet(() -> {
            CollectorItem collectorItem = new CollectorItem();
            collectorItem.setId(ObjectId.get());
            collectorItem.setCollectorId(perfToolsCollector.getId());
            collectorItem.setCollector(perfToolsCollector);
            collectorItem.setLastUpdated(System.currentTimeMillis());
            collectorItem.setEnabled(true);
            collectorItem.setPushed(true);
            collectorItem.setNiceName(testResultCollItem.getNiceName());
            collectorItem.setOptions(testResultCollItem.getOptions());
            collectorItem.setDescription(description);
            return collectorItem;
        }));
        return collectorItemRepository.save(optCollectorItem.get());
    }

    /**
     * Creates collector if not exists already
     */
    public Collector getPerfToolsCollector() {

        Optional<Collector> optCollector = Optional.ofNullable(collectorRepository.findByName(COLLECTOR_NAME));
        optCollector.ifPresent(collector -> collector.setLastExecuted(System.currentTimeMillis()));
        optCollector = Optional.ofNullable(optCollector.orElseGet(() -> {
            Collector collector = new Collector(COLLECTOR_NAME, CollectorType.AppPerformance);
            collector.setId(ObjectId.get());
            collector.setEnabled(true);
            collector.setOnline(true);
            collector.setLastExecuted(System.currentTimeMillis());
            return collector;
        }));
        return collectorRepository.save(optCollector.get());
    }

    /**
     * Get performance test metrics
     * @param testCapability
     */
    public LinkedHashMap<String,Object> getPerfMetrics(TestCapability testCapability) {

        LinkedHashMap<String,Object> metrics = new LinkedHashMap<>();
        long testCapabilitySecs = TimeUnit.MILLISECONDS.toSeconds(testCapability.getDuration());
        // If test execution duration less than a minute, calculate calls per minute from actual test execution seconds
        double callsPerMinuteVal = testCapabilitySecs > SIXTY_SECS ? actualTxnsPerSec * SIXTY_SECS : actualTxnsPerSec * testCapabilitySecs;
        // calculate errors per second from total number of errors of a test execution
        double actualErrorsPerSec = actualErrorsVal/testCapabilitySecs;
        double errorsPerMinuteVal = testCapabilitySecs > SIXTY_SECS ? actualErrorsPerSec * SIXTY_SECS : actualErrorsPerSec * testCapabilitySecs;
        double totalCallsVal = actualTxnsPerSec * testCapabilitySecs;
        txnHealthPercentVal = isTxnGoodHealth ? HEALTH_GOOD : HEALTH_CRITICAL;
        respTimeSeverityVal = isResponseTimeGood ? SEVERITY_GOOD : SEVERITY_CRITICAL;
        errRateSeverityVal = isErrorRateGood ? SEVERITY_GOOD : SEVERITY_CRITICAL;

        metrics.put(PERFORMANCE_METRICS.AVERAGE_RESPONSE_TIME.name(), Math.round(actualRespTime));
        metrics.put(PERFORMANCE_METRICS.CALLS_PER_MIN.name(), Math.round(callsPerMinuteVal));
        metrics.put(PERFORMANCE_METRICS.ERRORS_PER_MIN.name(), Math.round(errorsPerMinuteVal));
        metrics.put(PERFORMANCE_METRICS.ACTUAL_ERROR_RATE.name(), actualErrorRate);
        metrics.put(PERFORMANCE_METRICS.TOTAL_CALLS.name(), Math.round(totalCallsVal));
        metrics.put(PERFORMANCE_METRICS.TOTAL_ERRORS.name(), Math.round(actualErrorsVal));
        metrics.put(PERFORMANCE_METRICS.BUSINESS_TRANSACTION_HEALTH_PERCENT.name(), txnHealthPercentVal);
        metrics.put(PERFORMANCE_METRICS.NODE_HEALTH_PERCENT.name(), HEALTH_GOOD);
        metrics.put(PERFORMANCE_METRICS.ERROR_RATE_SEVERITY.name(), errRateSeverityVal);
        metrics.put(PERFORMANCE_METRICS.RESPONSE_TIME_SEVERITY.name(), respTimeSeverityVal);
        metrics.put(PERFORMANCE_METRICS.VIOLATION_OBJECT.name(), getPerfTestViolation());

        metrics.put(PERFORMANCE_METRICS.TARGET_RESPONSE_TIME.name(), targetRespTime);
        metrics.put(PERFORMANCE_METRICS.TARGET_TRANSACTION_PER_SEC.name(), targetTxnsPerSec);
        metrics.put(PERFORMANCE_METRICS.TARGET_ERROR_RATE_THRESHOLD.name(), targetErrorRate);
        return metrics;
    }

    /**
     * Get performance test violation details
     */
    public List getPerfTestViolation() {
        List<LinkedHashMap<Object, Object>> violationObjList = new ArrayList<>();
        LinkedHashMap<Object, Object> violationObjMap = new LinkedHashMap<>();
        if (!(isResponseTimeGood && isTxnGoodHealth && isErrorRateGood)){
            violationObjMap.put(VIOLATION_ATTRIBUTES.SEVERITY, STR_CRITICAL);
            violationObjMap.put(VIOLATION_ATTRIBUTES.INCIDENT_STATUS, STR_OPEN);
        }
        if(!violationObjMap.isEmpty()) {
            violationObjList.add(violationObjMap);
        }
        return violationObjList;
    }
}