package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.JobCollectorItem;
import org.bson.types.ObjectId;

import java.util.List;

public interface JobRepository<T extends JobCollectorItem> extends BaseCollectorItemRepository<T> {
    default T findJob(ObjectId collectorId, String instanceUrl, String jobName) {
        return findOne("collectorId eq " + quote(collectorId) + " and options.instanceUrl eq " + quote(instanceUrl) + " and options.jobName eq " + quote(jobName));
    }

    default T findJobByJobUrl(ObjectId collectorId, String jobUrl, String jobName) {
        return findOne("collectorId eq " + quote(collectorId) + " and options.jobUrl eq " + quote(jobUrl) + " and options.jobName eq " + quote(jobName));
    }

    default List<T> findEnabledJobs(ObjectId collectorId, String instanceUrl) {
        return findAll("collectorId eq " + quote(collectorId) + " and options.instanceUrl eq " + quote(instanceUrl) + " and enabled isTrue");
    }
}
