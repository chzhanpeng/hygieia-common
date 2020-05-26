package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CodeQuality;
import com.capitalone.dashboard.model.LibraryPolicyResult;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Repository for {@link CodeQuality} data.
 */
public interface LibraryPolicyResultsRepository extends QueryRepository<LibraryPolicyResult> {


    LibraryPolicyResult findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);
    LibraryPolicyResult findByCollectorItemId(ObjectId collectorItemId);
    List<LibraryPolicyResult> findByCollectorItemIdAndTimestampIsBetweenOrderByTimestampDesc(ObjectId collectorItemId, long beginDate, long endDate);
    List<LibraryPolicyResult> findByCollectorItemIdAndEvaluationTimestampIsBetweenOrderByTimestampDesc(ObjectId collectorItemId, long beginDate, long endDate);
    LibraryPolicyResult findTopByCollectorItemIdOrderByTimestampDesc(ObjectId collectorItemId);
}
