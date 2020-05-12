package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.EnvironmentComponent;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link EnvironmentComponent} repository.
 */
public interface EnvironmentComponentRepository extends QueryRepository<EnvironmentComponent> {

    /**
     * Finds the {@link EnvironmentComponent} collector item id, environment name and component name.
     *
     * @param collectorItemId collector item id
     * @param environmentName environment name
     * @param componentName component name
     * @return a {@link EnvironmentComponent}
     */
    default EnvironmentComponent findComponent(ObjectId collectorItemId, String environmentName, String componentName) {
        return findOne("collectorItemId eq " + quote(collectorItemId.toHexString()) + " and componentName eq " + quote(componentName) + " and environmentName eq " +quote( environmentName));
    }

    /**
     * Finds all {@link EnvironmentComponent}s for a given {@link com.capitalone.dashboard.model.CollectorItem}.
     *
     * @param collectorItemId collector item id
     * @return list of {@link EnvironmentComponent}
     */
    List<EnvironmentComponent> findByCollectorItemId(ObjectId collectorItemId);

    default List<EnvironmentComponent> findDeployedByCollectorItemIds(List<ObjectId> collectorItemIds) {
        String query = "deployed isTrue and collectorItemId in " + in(collectorItemIds.stream().map(ObjectId::toHexString).collect(Collectors.toList()));
        return findAll(query);
    }

    default List<EnvironmentComponent> findDeployedByCollectorItemIdAndComponentNameAndComponentVersion(ObjectId dashboardCollectorItemId, String componentName, String componentVersion) {
        return findAll("collectorItemId eq " + quote(dashboardCollectorItemId.toHexString()) + " and componentName eq " + quote(componentName) + " and componentVersion eq " + quote(componentVersion) + " and deployed isTrue");
    }

    default EnvironmentComponent findByUniqueKey(ObjectId collectorItemId, String componentName, String componentVersion, long deployTime) {
        return findOne("collectorItemId eq " + quote(collectorItemId.toHexString()) + " and componentName eq " + quote(componentName) + " and componentVersion eq " + quote(componentVersion) + " and deployTime eq " + deployTime);
    }

}
