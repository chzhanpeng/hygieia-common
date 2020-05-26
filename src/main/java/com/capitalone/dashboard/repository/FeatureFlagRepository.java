package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.FeatureFlag;

public interface FeatureFlagRepository extends QueryRepository<FeatureFlag> {

    FeatureFlag findByName(String name);

}
