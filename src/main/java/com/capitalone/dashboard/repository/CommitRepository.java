package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Commit;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Repository for {@link Commit} data.
 */
public interface CommitRepository extends QueryRepository<Commit> {

    /**
     * Finds the {@link Commit} with the given revision number for a specific
     * {@link com.capitalone.dashboard.model.CollectorItem}.
     *
     * @param collectorItemId collector item id
     * @param revisionNumber revision number
     * @return a {@link Commit}
     */
    Commit findByCollectorItemIdAndScmRevisionNumber(ObjectId collectorItemId, String revisionNumber);

    Long countCommitsByCollectorItemId(ObjectId collectorItemId);

    Commit findByScmUrlIgnoreCaseAndScmBranchIgnoreCaseAndScmRevisionNumber (String scmUrl, String scmBranch, String scmRevisionNumber);

    List<Commit> findByScmRevisionNumber (String scmUrl);

    List<Commit> findByScmUrlIgnoreCaseAndScmBranch (String scmUrl, String scmBranch);

    Commit findByScmRevisionNumberAndScmUrlIgnoreCaseAndScmBranchIgnoreCase(String scmRevisionNumber, String scmUrl, String branch);

    List<Commit> findAllByScmRevisionNumberAndScmUrlIgnoreCaseAndScmBranchIgnoreCaseOrderByTimestampAsc (String scmRevisionNumber, String scmUrl, String branch);

    default List<Commit> findByCollectorItemIdAndScmCommitTimestamp(ObjectId collectorItemId, Long scmCommitTimestampThreshold) {
        return findAll("collectorItemId eq " + quote(collectorItemId.toHexString()) + " and scmCommitTimestamp gt " + scmCommitTimestampThreshold);
    }

    List<Commit> findByScmUrlIgnoreCaseAndScmBranchIgnoreCaseAndScmCommitTimestampIsBetween(String scmUrl, String scmBranch, long beginDate, long endDate);

    List<Commit> findByCollectorItemIdAndScmCommitTimestampIsBetween(ObjectId collectorItemId, long beginDate, long endDate);

    List<Commit> findCommitsByCollectorItemIdAndTimestampAfterAndPullNumberIsNull(ObjectId collectorItemId, long beginDate);

    Commit findByScmRevisionNumberAndScmAuthorIgnoreCaseAndScmCommitLogAndScmCommitTimestamp(String scmRevisionNumber, String scmAuthor, String scmCommitLog, long scmCommitTimestamp);

    List<Commit> findAllByScmRevisionNumberAndScmAuthorIgnoreCaseAndScmCommitLogAndScmCommitTimestamp(String scmRevisionNumber, String scmAuthor, String scmCommitLog, long scmCommitTimestamp);
}
