package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CloudInstance;
import com.capitalone.dashboard.model.NameValue;

import java.util.Collection;
import java.util.List;

public interface CloudInstanceRepository extends QueryRepository<CloudInstance> {

    CloudInstance findByInstanceId(String instanceId);

    Collection<CloudInstance> findByTagsIn(List<NameValue> tags);

    default Collection<CloudInstance> findByTags(List<NameValue> tags) {
        return findByTagsIn(tags);
    }

    default Collection<CloudInstance> findByTagNameAndValue(String name, String value) {
        return findAll("tags.name eq " + quote(name) + " and tags.value eq " + quote(value));
    }

    Collection<CloudInstance> findByInstanceIdIn(List<String> instanceId);

    Collection<CloudInstance> findByAccountNumber(String accountNumber);

}
