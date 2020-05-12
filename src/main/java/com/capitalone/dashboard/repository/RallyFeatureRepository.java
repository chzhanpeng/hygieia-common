package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CodeQuality;
import com.capitalone.dashboard.model.RallyFeature;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Repository for {@link CodeQuality} data.
 */
public interface RallyFeatureRepository extends QueryRepository<RallyFeature> {

    /**
     * Finds the {@link RallyFeature} data point at the given timestamp for a specific
     * {@link com.capitalone.dashboard.model.CollectorItem}.
     *
     * @param collectorItemId collector item id
     * @param timestamp timstamp
     * @return a {@link RallyFeature}
     */
    RallyFeature findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);
    

    default List<RallyFeature> findByIterationLists(String projectId) {
        return findAll("projectId eq " + quote(projectId));
    }

    default RallyFeature findByRallyWidgetDetails(String projectId,String iterationId) {
        return findOne("projectId eq " + quote(projectId) + " and options.iterationId eq " + quote(iterationId));
    }

    default List<RallyFeature> findByProjectIterationId(Object collectorItemId) {
        return findAll("collectorItemId eq "  + quote(collectorItemId.toString()));
    }
    
    List<RallyFeature> findByCollectorItemIdAndRemainingDaysNot(Object collectorItemId, int remainginDays);
    

	List<RallyFeature> findByProjectId(String projectId);
    
}

