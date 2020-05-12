package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CodeQuality;
import com.capitalone.dashboard.model.score.ScoreMetric;
import org.bson.types.ObjectId;

/**
 * Repository for {@link CodeQuality} data.
 */
public interface ScoreRepository extends QueryRepository<ScoreMetric> {

    /**
     * Finds the {@link ScoreMetric} data point at the given timestamp for a specific
     * {@link com.capitalone.dashboard.model.CollectorItem}.
     *
     * @param collectorItemId collector item id
     * @param timestamp timstamp
     * @return a {@link CodeQuality}
     */
    ScoreMetric findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);

    /**
     * Finds all {@link ScoreMetric}s for a given {@link com.capitalone.dashboard.model.CollectorItem}.
     *
     * @param collectorItemId collector item id
     * @return list of {@link ScoreMetric}
     */
    ScoreMetric findByCollectorItemId(ObjectId collectorItemId);

}
