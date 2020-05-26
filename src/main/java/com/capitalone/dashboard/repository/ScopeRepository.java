package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Scope;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repository for {@link Scope}.
 */
public interface ScopeRepository extends QueryRepository<Scope> {

	List<Scope> findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(
			ObjectId collectorId, String changeDate);

	default List<Scope> getScopeIdById(String pId) {
		return findAll("pId eq " + quote(pId));
	}

	List<Scope> findByOrderByProjectPathDesc();

	default List<Scope> getScopeById(String pId) {
		return findAll("pId eq " + quote(pId));
	}

	List<Scope> findByCollectorId(ObjectId collectorId);

	Page<Scope> findAllByCollectorIdAndNameContainingIgnoreCase(ObjectId collectorId, String name, Pageable pageable);

	Scope findByCollectorIdAndPId(ObjectId collectorId, String pId);
}
