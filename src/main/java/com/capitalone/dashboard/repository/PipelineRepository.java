package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Pipeline;
import org.bson.types.ObjectId;

import java.util.List;

public interface PipelineRepository extends QueryRepository<Pipeline> {

    Pipeline findByCollectorItemId(ObjectId collectorItemId);

    List<Pipeline> findByCollectorItemIdIn(List<ObjectId> collectorItemIds);

}
