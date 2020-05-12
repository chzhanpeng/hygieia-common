package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CloudVirtualNetwork;
import com.capitalone.dashboard.model.NameValue;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.List;

public interface CloudVirtualNetworkRepository extends QueryRepository<CloudVirtualNetwork> {

    default CloudVirtualNetwork findByCollectorItemId(ObjectId collectorItemId) {
        return findOne("collectorItemId eq " + quote(collectorItemId.toHexString()));
    }

    Collection<CloudVirtualNetwork> findByTagsIn(List<NameValue> tags);


    default Collection<CloudVirtualNetwork> findByTags(List<NameValue> tags) {
        return findByTagsIn(tags);
    }

    CloudVirtualNetwork findByVirtualNetworkId(String virtualNetworkId);

    default Collection<CloudVirtualNetwork> findByTagNameAndValue(String name, String value) {
        return findAll("tags.name eq " + quote(name) + " and tags.value eq " + quote(value));
    }

    Collection<CloudVirtualNetwork> findByvirtualNetworkIdIn(List<String> virtualNetworkId);

    Collection<CloudVirtualNetwork> findByAccountNumber(String accountNumber);

}
