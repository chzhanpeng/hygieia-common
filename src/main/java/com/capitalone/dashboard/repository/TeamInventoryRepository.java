package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.model.TeamInventory;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Repository for {@link Scope}.
 */
public interface TeamInventoryRepository extends QueryRepository<TeamInventory> {

	List<TeamInventory> findByCollectorId(ObjectId collectorId);

	TeamInventory findByNameAndTeamId(String name, String teamId);

}
