package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Team;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repository for {@link Team}.
 */
public interface TeamRepository extends QueryRepository<Team> {

    Team findByTeamId(String teamId);

    Team findByName(String name);

    /**
     * This essentially returns the max change date from the collection, based
     * on the last change date (or default delta change date property) available
     *
     * @param collectorId
     *            Collector ID of source system collector
     * @param changeDate
     *            Last available change date or delta begin date property
     * @return A single Change Date value that is the maximum value of the
     *         existing collection
     */

    default List<Team> findTopByChangeDateDesc(ObjectId collectorId, String changeDate) {
        return findAll("collectorId eq " + quote(collectorId.toHexString()) + " and changeDate gt " + quote(changeDate) + " and assetState equalsIgnoreCase 'active'");
    }

    List<Team> findByCollectorId(ObjectId collectorId);

    Page<Team> findAllByCollectorIdAndNameContainingIgnoreCase(ObjectId collectorId, String name, Pageable pageable);
}
