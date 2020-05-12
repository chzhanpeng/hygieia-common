package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Incident;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

/**
 * Repository for {@link Incident} data.
 */

public interface IncidentRepository extends QueryRepository<Incident> {

    default Incident findByIncidentID(String incidentID) {
        return findOne("incidentID eq " + quote(incidentID));
    }

    default List<Incident> findBySeverity(String[] severityValues){
        return findAll("severity in " + in(Arrays.asList(severityValues)));
    }

    default Page<Incident> findIncidentsBySeverityAndOpenTimeBetween (String[] severityValues, long startDate, long endDate, Pageable pageable) {
        String query = "severity in " + in(Arrays.asList(severityValues));
        query = and(query) + "openTime gt " + startDate + " and openTime lt " + endDate;
        query = page(query, pageable);
        return  findAllAsPage(query);
    }

    default long countIncidentsBySeverityAndOpenTimeBetween (String[] severityValues, long startDate, long endDate) {
        return countBySeverityInAndOpenTimeBetween (severityValues, startDate, endDate);
    }

    long countBySeverityInAndOpenTimeBetween(String[] severityValues, long startDate, long endDate);


    default List<Incident> findByCollectorItemId(List<ObjectId> collectorItemIds) {
        return findAllByCollectorItemIdIn(collectorItemIds);
    }

    List<Incident> findAllByCollectorItemIdIn(List<ObjectId> collectorItemIds);
}
