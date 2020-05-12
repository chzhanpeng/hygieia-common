package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CloudSubNetwork;
import com.capitalone.dashboard.model.NameValue;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.List;

public interface CloudSubNetworkRepository extends  QueryRepository<CloudSubNetwork> {

    default CloudSubNetwork findByCollectorItemId(ObjectId collectorItemId) {
        return findOne("collectorItemId eq " + quote(collectorItemId.toHexString()));
    }

    Collection<CloudSubNetwork> findByTagsIn(List<NameValue> tags);


    default Collection<CloudSubNetwork> findByTags(List<NameValue> tags) {
        return findByTagsIn(tags);
    }

    CloudSubNetwork findBySubnetId(String subnetId);

    default Collection<CloudSubNetwork> findByTagNameAndValue(String name, String value) {
        return findAll("tags.name eq " + quote(name) + " and tags.value eq " + quote(value));
    }

    Collection<CloudSubNetwork> findBySubnetIdIn(List<String> subnetId);

    Collection<CloudSubNetwork> findByAccountNumber(String accountNumber);

}
