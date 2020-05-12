package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.EnvironmentStatus;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * {@link EnvironmentStatus} repository.
 */
public interface EnvironmentStatusRepository extends QueryRepository<EnvironmentStatus> {

    /**
     * Finds all {@link EnvironmentStatus}es for a given {@link com.capitalone.dashboard.model.CollectorItem}.
     *
     * @param collectorItemId collector item id
     * @return list of {@link EnvironmentStatus}es.
     */
    List<EnvironmentStatus> findByCollectorItemId(ObjectId collectorItemId);
}
