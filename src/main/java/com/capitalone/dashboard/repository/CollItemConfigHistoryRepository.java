package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CollectorItemConfigHistory;
import org.bson.types.ObjectId;

import java.util.List;

public interface CollItemConfigHistoryRepository extends QueryRepository<CollectorItemConfigHistory>  {
	List<CollectorItemConfigHistory> findByCollectorItemIdAndTimestampIsBetweenOrderByTimestampDesc(ObjectId collectorItemId, long beginDate, long endDate);
	CollectorItemConfigHistory findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);
}
