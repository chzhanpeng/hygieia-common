package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.TestResult;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestResultRepositoryTest extends MongoServerBaseRepositoryTest {

    @Autowired
    TestResultRepository testResultRepository;

    @Before
    public void clean() {
        testResultRepository.deleteAll();
    }

    @Test
    public void testFindByCollectorItemIdAndExecutionId() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        TestResult tr = new TestResult();
        tr.setCollectorItemId(id1);
        tr.setExecutionId("exe1");
        tr.setDescription("test result 1");
        testResultRepository.save(tr);

        assertEquals("test result 1", testResultRepository.findByCollectorItemIdAndExecutionId(id1, "exe1").getDescription());
    }

    @Test
    public void testFindTop1() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        TestResult tr1 = new TestResult();
        tr1.setCollectorItemId(id1);
        tr1.setDescription("test result 1");
        tr1.setTimestamp(1000);
        TestResult tr2 = new TestResult();
        tr2.setCollectorItemId(id1);
        tr2.setTimestamp(2000);
        tr2.setDescription("test result 2");
        testResultRepository.save(Arrays.asList(tr1, tr2));

        assertEquals("test result 2", testResultRepository.findTop1ByCollectorItemIdOrderByTimestampDesc(id1).getDescription());
    }

    @Test
    public void testFindByEnvName() {
        TestResult tr1 = new TestResult();
        tr1.setTimestamp(1000);
        tr1.setDescription("test result 1");
        tr1.setTargetEnvName("env");
        TestResult tr2 = new TestResult();
        tr2.setTimestamp(2000);
        tr2.setDescription("test result 2");
        testResultRepository.save(Arrays.asList(tr1, tr2));

        assertEquals(0, testResultRepository.findByTimestampIsAfterAndTargetEnvNameExists(3000, true).size());
        assertEquals(1, testResultRepository.findByTimestampIsAfterAndTargetEnvNameExists(500, true).size());
        assertEquals(1, testResultRepository.findByTimestampIsAfterAndTargetEnvNameExists(500, false).size());

    }
}
