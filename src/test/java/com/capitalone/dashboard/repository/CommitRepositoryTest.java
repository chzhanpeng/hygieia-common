package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Commit;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class CommitRepositoryTest extends MongoServerBaseRepositoryTest {

    @Autowired
    private CommitRepository commitRepository;

    @Before
    public void clean() {
        commitRepository.deleteAll();
    }

    @Test
    public void testFindByCollectorItemIdAndScmRevisionNumber() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        Commit commit1 = makeCommit(id1, "1", "author1", "commit 1");
        commitRepository.save(commit1);
        assertEquals("commit 1", commitRepository.findByCollectorItemIdAndScmRevisionNumber(id1, "1").getScmCommitLog());
    }

    @Test
    public void testCountCommitsByCollectorItemId() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        Commit commit1 = makeCommit(id1, "1", "author1", "commit 1");
        Commit commit2 = makeCommit(id1, "2", "author1", "commit 2");
        commitRepository.save(commit1);
        commitRepository.save(commit2);
        assertEquals(2, (long) commitRepository.countCommitsByCollectorItemId(id1));
        Commit commit3 = makeCommit(id1, "3", "author1", "commit 3");
        commitRepository.save(commit3);
        assertEquals(3, (long) commitRepository.countCommitsByCollectorItemId(id1));
    }

    @Test
    public void testFindByScmUrlIgnoreCaseAndScmBranchIgnoreCaseAndScmRevisionNumber() {
        Commit commit = makeCommit("github.com/owner/repo", "main", "1");
        commitRepository.save(commit);
        Assert.assertNull(commitRepository.findByScmUrlIgnoreCaseAndScmBranchIgnoreCaseAndScmRevisionNumber("github", "main", "1" ));
        Assert.assertNotNull(commitRepository.findByScmUrlIgnoreCaseAndScmBranchIgnoreCaseAndScmRevisionNumber("Github.com/Owner/Repo", "main", "1" ));
        Assert.assertNotNull(commitRepository.findByScmUrlIgnoreCaseAndScmBranchIgnoreCaseAndScmRevisionNumber("github.com/owner/repo", "main", "1" ));
        Assert.assertNotNull(commitRepository.findByScmRevisionNumberAndScmUrlIgnoreCaseAndScmBranchIgnoreCase("1", "github.com/owner/repo", "main"));
        Assert.assertNull(commitRepository.findByScmRevisionNumberAndScmUrlIgnoreCaseAndScmBranchIgnoreCase("2", "github.com/owner/repo", "main"));
    }

    @Test
    public void testFindByTimestamp() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        commitRepository.save(makeCommit(id1, 10));
        commitRepository.save(makeCommit(id1, 100));
        commitRepository.save(makeCommit(id1, 1000));
        assertEquals(0, commitRepository.findByCollectorItemIdAndScmCommitTimestampIsBetween(id1,2000, 3000).size());
        assertEquals(3, commitRepository.findByCollectorItemIdAndScmCommitTimestampIsBetween(id1,1, 3000).size());
    }

    private Commit makeCommit(ObjectId collectorItemId, String scmRevisionNumber, String author, String log ) {
        Commit commit = new Commit();
        commit.setCollectorItemId(collectorItemId);
        commit.setScmRevisionNumber(scmRevisionNumber);
        commit.setScmAuthor(author);
        commit.setScmCommitLog(log);
        return commit;
    }

    private Commit makeCommit(String scmUrl, String scmBranch, String scmRevisionNumber) {
        Commit commit = new Commit();
        commit.setScmUrl(scmUrl);
        commit.setScmBranch(scmBranch);
        commit.setScmRevisionNumber(scmRevisionNumber);
        return commit;
    }

    private Commit makeCommit(ObjectId collectorItemId, long timestamp) {
        Commit commit = new Commit();
        commit.setCollectorItemId(collectorItemId);
        commit.setScmCommitTimestamp(timestamp);
        return commit;
    }
}
