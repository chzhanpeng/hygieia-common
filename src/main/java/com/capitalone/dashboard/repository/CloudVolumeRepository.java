package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CloudVolumeStorage;
import com.capitalone.dashboard.model.NameValue;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.List;

public interface CloudVolumeRepository extends QueryRepository<CloudVolumeStorage> {

    default Collection<CloudVolumeStorage> findByCollectorItemId(ObjectId collectorItemId) {
        return findAll("collectorItemId eq " + quote(collectorItemId.toHexString()));
    }

    CloudVolumeStorage findByVolumeId(String volumeId);

    Collection<CloudVolumeStorage> findByTagsIn(List<NameValue> tags);

    default Collection<CloudVolumeStorage> findByTags(List<NameValue> tags) {
        return findByTagsIn(tags);
    }

    default Collection<CloudVolumeStorage> findByTagNameAndValue(String name, String value) {
        return findAll("tags.name eq " + quote(name) + " and tags.value eq " + quote(value));
    }

    Collection<CloudVolumeStorage> findByVolumeIdIn(List<String> volumeId);

    Collection<CloudVolumeStorage> findByAttachInstancesIn(List<String> attachInstances);

    Collection<CloudVolumeStorage> findByAccountNumber(String accountNumber);

}
