package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.Dashboard;
import com.capitalone.dashboard.model.DashboardType;
import com.capitalone.dashboard.model.Owner;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Dashboard} repository.
 */
public interface DashboardRepository extends QueryRepository<Dashboard> {
	
	List<Dashboard> findByOwner(String owner);

	List<Dashboard> findByOwners(Owner owner);
	List<Dashboard> findByOwnersAndTypeContainingIgnoreCase(Owner owner, String type);

	List<Dashboard> findByTitle(String title);
	Dashboard findByTitleAndType(String title, DashboardType type);

    List<Dashboard> findByApplicationComponentsIn(Collection<Component> components);

//    default List<Dashboard> findByApplicationComponentIdsIn(Collection<ObjectId> componentIds) {
//    	String query = "widgets.componentId in ('" + String.join("','", componentIds.stream().map(ObjectId::toHexString).toArray(String[]::new)) + "')";
//    	return findAll(query);
//	}

	default List<Dashboard> findByApplicationComponentIdsIn(Collection<ObjectId> componentIds) {
		String query = "application.components.id in " + in(componentIds.stream().map(ObjectId::toHexString).collect(Collectors.toList()));
		return findAll(query);
	}

	default List<Dashboard> findTeamDashboards() {
    	return findAll("type eq 'Team' or type isNull");
	}

	default List<Dashboard> findProductDashboardsByTeamDashboardCollectorItemId(String teamDashboardCollectorItemId) {
    	return findAll("widgets.options.teams.collectorItemId eq " + quote(teamDashboardCollectorItemId));
	}

	Iterable<Dashboard> findAllByConfigurationItemBusServName(String configurationItem);

	Iterable<Dashboard> findAllByConfigurationItemBusAppName(String configurationItem);

	Iterable<Dashboard> findAllByConfigurationItemBusServNameIn(List<String> configurationItemList);

	Iterable<Dashboard> findAllByConfigurationItemBusServNameAndConfigurationItemBusAppName(String appName, String compName);

	Dashboard findByConfigurationItemBusServNameIgnoreCaseAndConfigurationItemBusAppNameIgnoreCase(String appName, String compName);
	List<Dashboard> findAllByTypeAndConfigurationItemBusServNameContainingIgnoreCase(DashboardType type, String appName);
	List<Dashboard> findAllByConfigurationItemBusServNameContainingIgnoreCaseAndConfigurationItemBusAppNameContainingIgnoreCase(String appName, String compName);
	List<Dashboard> findAllByTypeAndConfigurationItemBusServNameContainingIgnoreCaseAndConfigurationItemBusAppNameContainingIgnoreCase(DashboardType type, String appName, String compName);

	Page<Dashboard> findAll(Pageable page);
	Page<Dashboard> findAllByTypeContainingIgnoreCase(String type,Pageable pageable);

	Page<Dashboard> findAllByTitleContainingIgnoreCase(String name, Pageable pageable);
	Page<Dashboard> findAllByTypeContainingIgnoreCaseAndTitleContainingIgnoreCase(String type, String title, Pageable pageable);

	List<Dashboard> findAllByTitleContainingIgnoreCase(String name);
	List<Dashboard> findAllByTypeContainingIgnoreCaseAndTitleContainingIgnoreCase(String type, String title);

	long count();
	long countByTypeContainingIgnoreCase(String type);

	Page<Dashboard> findByOwners(Owner owner, Pageable pageable);
	Page<Dashboard> findByOwnersAndTypeContainingIgnoreCase(Owner owner, String type, Pageable pageable);

	List<Dashboard> findByOwnersAndTitleContainingIgnoreCase(Owner owner, String name);
	List<Dashboard> findByOwnersAndTypeContainingIgnoreCaseAndTitleContainingIgnoreCase(Owner owner, String type, String title);

	Page<Dashboard> findByOwnersAndTitleContainingIgnoreCase(Owner owner, String title, Pageable pageable);
	Page<Dashboard> findByOwnersAndTypeContainingIgnoreCaseAndTitleContainingIgnoreCase(Owner owner, String type, String title, Pageable pageable);

	Iterable<Dashboard> findAllByType(DashboardType dashboardType);
}
