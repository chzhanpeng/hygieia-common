package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.RallyBurnDownData;

public interface RallyBurnDownRepository extends QueryRepository<RallyBurnDownData>{
	
	RallyBurnDownData findByIterationIdAndProjectId(String iterationId, String projectId);
	
}
	
