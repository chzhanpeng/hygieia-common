package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.BinaryArtifact;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface BinaryArtifactRepository extends QueryRepository<BinaryArtifact> {

    String ARTIFACT_NAME = "artifactName";
    String ARTIFACT_GROUPID = "artifactGroupId";
    String ARTIFACT_MODULE = "artifactModule";
    String ARTIFACT_VERSION = "artifactVersion";
    String ARTIFACT_CLASSIFIER = "artifactClassifier";
    String ARTIFACT_EXTENSION = "artifactExtension";
    String BUILD_INFO_ID = "buildInfo.id";

    Iterable<BinaryArtifact> findByCollectorItemId(ObjectId collectorItemId);

    default Iterable<BinaryArtifact> findByAttributes(Object collectorItemId, String artifactGroupId, String artifactModule, String artifactVersion, String artifactName, String artifactClassifier, String artifactExtension) {
        String query = "collectorItemId eq " + quote(collectorItemId.toString());
        query = and(query) + getCommonQuery(artifactGroupId, artifactModule, artifactVersion, artifactName, artifactClassifier, artifactExtension);
        return findAll(query);
    }

    default Iterable<BinaryArtifact> findByAttributes(String artifactGroupId, String artifactModule, String artifactVersion, String artifactName, String artifactClassifier, String artifactExtension) {
        String query = getCommonQuery(artifactGroupId, artifactModule, artifactVersion, artifactName, artifactClassifier, artifactExtension);
        return findAll(query);
    }

    default String getCommonQuery(String artifactGroupId, String artifactModule, String artifactVersion, String artifactName, String artifactClassifier, String artifactExtension) {
        String query = "artifactGroupId eq " + quote(artifactGroupId);
        query = and(query) + "artifactModule eq " + quote(artifactModule);
        query = and(query) + "artifactVersion eq " + quote(artifactVersion);
        query = and(query) + "artifactName eq " + quote(artifactName);
        query = and(query) + "artifactClassifier eq " + quote(artifactClassifier);
        return and(query) + "artifactExtension eq " + quote(artifactExtension);
    }

    default Iterable<BinaryArtifact> findByBuildInfoId(ObjectId artifactBuildId) {
        return findAll("buildInfo.id eq " + quote(artifactBuildId.toHexString()));
    }

    Iterable<BinaryArtifact> findByArtifactNameAndTimestampGreaterThan(String artifactName, Long timestamp);

    Iterable<BinaryArtifact> findByArtifactNameAndArtifactExtensionAndTimestampGreaterThan(String artifactName, String artifactExtension, Long timestamp);

    default Iterable<BinaryArtifact> findByMetadataBuildUrl(String buildUrl) {
        return findAll("metadata.buildUrl equalsIgnoreCase " + quote(buildUrl));
    }

    default Iterable<BinaryArtifact> findByAttributes(Map<String, Object> attributes) {
        String query = "";
        for (Map.Entry<String, Object> e : attributes.entrySet()) {
            query = and(query) + e.getKey() + " eq " + quote(e.getValue());
        }
        return findAll(query);
    }
	
	BinaryArtifact findBinaryArtifactByCollectorItemIdAndArtifactVersion(ObjectId collectorItemId, String artifactVersion);
	BinaryArtifact findTopByCollectorItemIdAndBuildInfosIsNotEmptyOrderByTimestampDesc(ObjectId collectorItemId, Sort sort);
	BinaryArtifact findTopByCollectorItemIdAndArtifactVersionOrderByTimestampDesc(ObjectId collectorItemId, String artifactVersion);
    Iterable<BinaryArtifact> findByArtifactNameAndArtifactVersionAndCreatedTimeStamp(String artifactName, String artifactVersion,Long timestamp);
    Iterable<BinaryArtifact> findByArtifactNameAndArtifactVersion(String artifactName, String artifactVersion);
    List<BinaryArtifact> findByCollectorItemIdAndTimestampIsBetweenOrderByTimestampDesc(ObjectId collectorItemId, long beginDate, long endDate);
    BinaryArtifact findTopByCollectorItemIdOrderByTimestampDesc(ObjectId collectorItemId);

}
