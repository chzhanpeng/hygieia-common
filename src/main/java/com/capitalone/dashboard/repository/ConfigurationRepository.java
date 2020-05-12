package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Configuration;


public interface ConfigurationRepository extends QueryRepository<Configuration>{

	Configuration findByCollectorName(String collectorNiceName);
	
}
