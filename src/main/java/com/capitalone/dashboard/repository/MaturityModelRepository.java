package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.MaturityModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface MaturityModelRepository extends QueryRepository<MaturityModel> {
    MaturityModel findByProfile(String profile);

    default List<String> getAllProfiles() {
        List<MaturityModel> models = IterableUtils.toList(findAll());
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(MaturityModel::getProfile).collect(Collectors.toList());
    }
}
