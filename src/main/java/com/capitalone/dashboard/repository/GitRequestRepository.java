package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.GitRequest;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Repository for {@link GitRequest} data.
 */
public interface GitRequestRepository  extends QueryRepository<GitRequest> {

    List<GitRequest> findByCollectorItemIdAndRequestType(ObjectId collectorItemId, String requestType);

    default List<GitRequest> findRequestNumberAndLastUpdated(ObjectId collectorItemId, String requestType) {
        return findAll("collectorItemId eq " + quote(collectorItemId) +" and requestType eq " + quote(requestType));
    }

    default List<GitRequest> findNonMergedRequestNumberAndLastUpdated(ObjectId collectorItemId) {
        return findAll("collectorItemId eq " + quote(collectorItemId) +" and state ne 'merged'");
    }

    default List<GitRequest> findRequestNumberAndLastUpdated(ObjectId collectorItemId) {
        return findAll("collectorItemId eq " + quote(collectorItemId));
    }

    GitRequest findByCollectorItemIdAndScmRevisionNumber(ObjectId collectorItemId, String revisionNumber);

    GitRequest findByCollectorItemIdAndNumberAndRequestType(ObjectId collectorItemId, String number, String requestType);

    default List<GitRequest> findByCollectorItemIdAndScmCommitTimestamp(ObjectId collectorItemid, Long scmCommitTimestampThreshold) {
        return findAll("collectorItemId eq " + quote(collectorItemid) + " and scmCommitTimestamp gt " + scmCommitTimestampThreshold);
    }

    GitRequest findByCollectorItemIdAndNumber(ObjectId collectorItemId, String number);

    List<GitRequest> findByScmUrlIgnoreCaseAndScmBranchIgnoreCaseAndCreatedAtGreaterThanEqualAndMergedAtLessThanEqual(String scmUrl, String scmBranch, long beginDt, long endDt);

    List<GitRequest> findByScmUrlIgnoreCaseAndScmBranchIgnoreCase(String scmUrl, String scmBranch);

    List<GitRequest> findByScmRevisionNumber(String revisionNumber);

    List<GitRequest> findByCollectorItemIdAndMergedAtIsBetween(ObjectId collectorItemId, long beginDate, long endDate);

    GitRequest findByScmUrlIgnoreCaseAndScmBranchIgnoreCaseAndNumberAndRequestTypeIgnoreCase(String scmUrl, String scmBranch, String number, String requestType);

    default GitRequest findByScmRevisionNumberOrScmMergeEventRevisionNumber(String revisionNumber) {
        return findOne("scmRevisionNumber eq " + quote(revisionNumber) + " or scmMergeEventRevisionNumber eq " + quote(revisionNumber));
    }

    default GitRequest findByCommitScmRevisionNumber(String revisionNumber) {
        return findOne("commits.scmRevisionNumber eq " + quote(revisionNumber));
    }
}
