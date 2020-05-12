package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.ScopeOwnerCollectorItem;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;

/**
 * CollectorItem repository for {@link ScopeOwnerCollectorItem}.
 */
public interface ScopeOwnerRepository extends BaseCollectorItemRepository<ScopeOwnerCollectorItem> {

	default ScopeOwnerCollectorItem findTeamCollector(ObjectId collectorId, String teamId) {
		return findOne("collectorId eq " + quote(collectorId.toHexString()) + " and " + "options.teamId eq " + quote(teamId) + " and options.assetState equalsIgnoreCase 'active'");
	}

	default ScopeOwnerCollectorItem findTeamCollector(ObjectId collectorId, String teamId, String name) {
		return findOne("collectorId eq " + quote(collectorId.toHexString()) + " and " + "options.teamId eq " + quote(teamId) + " and options.name eq " + quote(name) + " and options.assetState equalsIgnoreCase 'active'");
	}

	default List<ScopeOwnerCollectorItem> findEnabledTeamCollectors(ObjectId collectorId, String teamId) {
		return findAll("collectorId eq " + quote(collectorId.toHexString()) + " and " + "options.teamId eq " + quote(teamId) + " and enabled isTrue and options.assetState equalsIgnoreCase 'active'");
	}

	default List<ScopeOwnerCollectorItem> findTopByChangeDateDesc(ObjectId collectorId, String changeDate) {
		return findAll("collectorId eq " + quote(collectorId.toHexString()) + " and options.changeDate gt " + quote(changeDate) + " and " + "options.assetState equalsIgnoreCase 'active'");
	}

	default List<ScopeOwnerCollectorItem> getTeamIdById(String teamId) {
		return findAll("options.teamId eq " + quote(teamId));
	}

	default List<ScopeOwnerCollectorItem> delete(String assetState) {
		List<ScopeOwnerCollectorItem> items = findAll("options.assetState equalsIgnoreCase " + quote(assetState));
		if (!CollectionUtils.isEmpty(items)) {
			deleteAll(items);
			return items;
		}
		return Collections.emptyList();
	}
}
