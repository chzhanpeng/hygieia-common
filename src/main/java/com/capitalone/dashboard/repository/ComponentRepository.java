package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Component;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Component} repository.
 */
public interface ComponentRepository extends QueryRepository<Component> {
    Logger LOGGER = LoggerFactory.getLogger(ComponentRepository.class);

    default List<Component> findByCollectorTypeAndCollectorItemId(CollectorType type, ObjectId collectorItemId) {
        return findAll("collectorItems." + type + ".id eq " + quote(collectorItemId.toHexString()));
    }

    default List<Component> findBySCMCollectorItemId(ObjectId scmCollectorItemId) {
        return findByCollectorTypeAndCollectorItemId(CollectorType.SCM, scmCollectorItemId);
    }

    default List<Component> findByBuildCollectorItemId(ObjectId buildCollectorItemId) {
        return findByCollectorTypeAndCollectorItemId(CollectorType.Build, buildCollectorItemId);
    }

    default List<Component> findByDeployCollectorItemId(ObjectId deployCollectorItemId) {
        return findByCollectorTypeAndCollectorItemId(CollectorType.Deployment, deployCollectorItemId);
    }

    default List<Component> findByIncidentCollectorItems(boolean enabled) {
        return findAll("collectorItems.Incident.enabled eq " + enabled);
    }


    default List<Component> findByCollectorTypeAndItemIdIn(CollectorType collectorType, List<ObjectId> collectorItemIds) {
        String query = "collectorItems." + collectorType + ".id in " + in(collectorItemIds.stream().map(ObjectId::toHexString).collect(Collectors.toList()));
        return findAll(query);
    }

    default List<Component> findComponents(Collector collector) {
        return findAll("collectorItems." + collector.getCollectorType() + ".collectorId eq " + quote(collector.getId().toHexString()));
    }

    default List<Component> findComponents(CollectorType collectorType) {
        return findAll("collectorItems." + collectorType + " isNotNull");
    }



    default List<Component> findComponents(Collector collector, CollectorItem collectorItem) {
        return findComponents(collector.getId(), collector.getCollectorType(), collectorItem.getId());
    }


    default List<Component> findComponents(ObjectId collectorId, CollectorType collectorType, CollectorItem collectorItem) {
        return findComponents(collectorId, collectorType, collectorItem.getId());
    }

    default List<Component> findComponents(ObjectId collectorId, CollectorType collectorType, ObjectId collectorItemId) {
        return findAll("collectorItems." + collectorType + ".id eq " + quote(collectorItemId.toHexString()));
    }

}