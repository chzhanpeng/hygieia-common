package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Performance;
import org.bson.types.ObjectId;

import java.util.List;

public interface PerformanceRepository extends QueryRepository<Performance> {

    Performance findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);
    List<Performance> findByCollectorItemId (ObjectId collectorItemId);


}
