package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.score.ScoreCollectorItem;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Repository for {@link ScoreCollectorItem}s.
 */
public interface ScoreCollectorItemRepository extends BaseCollectorItemRepository<ScoreCollectorItem> {

  /**
   * Finds all enabled {@link ScoreCollectorItem}s
   *
   * @param collectorId ID
   * @return list of {@link ScoreCollectorItem}s
   */

  default List<ScoreCollectorItem> findEnabledScores(ObjectId collectorId) {
    return findAll("collectorId eq " + quote(collectorId.toHexString()) + " and enabled isTrue");
  }

  default ScoreCollectorItem findCollectorItemByCollectorIdAndDashboardId(ObjectId collectorId, ObjectId dashboardId) {
    return findOne("collectorId eq " + quote(collectorId.toHexString()) + " and options.dashboardId eq " + quote(dashboardId.toHexString()));
  }

}
