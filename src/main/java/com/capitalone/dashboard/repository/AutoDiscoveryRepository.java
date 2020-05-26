package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.AutoDiscovery;

import java.util.List;

/**
 * {@link AutoDiscovery} repository.
 */
public interface AutoDiscoveryRepository extends QueryRepository<AutoDiscovery> {

	List<AutoDiscovery> findByMetaDataTitle(String title);
	AutoDiscovery findByMetaDataTitleAndMetaDataType(String title, String type);

}
