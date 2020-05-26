package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.FortifyScanReport;
import org.bson.types.ObjectId;

import java.util.List;

public interface FortifyScanRepository extends QueryRepository<FortifyScanReport>{
	
	FortifyScanReport findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);

	List<FortifyScanReport> findByCollectorItemId(ObjectId id);
}
