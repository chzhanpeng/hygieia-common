package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.GitRequest;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GitRequestRepositoryTest extends MongoServerBaseRepositoryTest {

    @Autowired
    private GitRequestRepository gitRequestRepository;

    @Before
    public void clean() {
        gitRequestRepository.deleteAll();
    }

    @Test
    public void testFindByCollectorItemIdAndRequestType() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        GitRequest pr1 = new GitRequest();
        pr1.setCollectorItemId(id1);
        pr1.setRequestType("pull");
        GitRequest pr2 = new GitRequest();
        pr2.setCollectorItemId(id1);
        pr2.setRequestType("pull");
        GitRequest issue1 = new GitRequest();
        issue1.setCollectorItemId(id1);
        issue1.setRequestType("issue");
        gitRequestRepository.save(Arrays.asList(pr1, pr2, issue1));

        assertEquals(2, gitRequestRepository.findByCollectorItemIdAndRequestType(id1, "pull").size());
        assertEquals(1, gitRequestRepository.findByCollectorItemIdAndRequestType(id1, "issue").size());
    }

    @Test
    public void testFindByScmRevisionNumber() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        GitRequest pr1 = new GitRequest();
        pr1.setCollectorItemId(id1);
        pr1.setScmRevisionNumber("111");
        pr1.setScmCommitLog("pr 1");
        gitRequestRepository.save(pr1);

        assertEquals("pr 1", gitRequestRepository.findByCollectorItemIdAndScmRevisionNumber(id1, "111").getScmCommitLog());
        assertEquals(1, gitRequestRepository.findByScmRevisionNumber("111").size());
    }

    @Test
    public void testFindByNumber() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        GitRequest pr1 = new GitRequest();
        pr1.setCollectorItemId(id1);
        pr1.setNumber("111");
        pr1.setRequestType("pull");
        pr1.setScmCommitLog("pr 1");
        gitRequestRepository.save(pr1);

        assertEquals("pr 1", gitRequestRepository.findByCollectorItemIdAndNumber(id1, "111").getScmCommitLog());
        assertEquals("pr 1", gitRequestRepository.findByCollectorItemIdAndNumberAndRequestType(id1, "111", "pull").getScmCommitLog());
        assertNull(gitRequestRepository.findByCollectorItemIdAndNumberAndRequestType(id1, "111", "issue"));
    }

    @Test
    public void testFindByUrlAndBranch() {
        GitRequest pr1 = new GitRequest();
        pr1.setScmUrl("github.com");
        pr1.setScmBranch("main");
        pr1.setNumber("1");
        GitRequest pr2 = new GitRequest();
        pr2.setScmUrl("github.com");
        pr2.setScmBranch("main");
        pr2.setNumber("2");
        GitRequest pr3 = new GitRequest();
        pr3.setScmUrl("github.com");
        pr3.setScmBranch("dev");
        pr3.setNumber("3");
        gitRequestRepository.save(Arrays.asList(pr1, pr2, pr3));

        assertEquals(2, gitRequestRepository.findByScmUrlIgnoreCaseAndScmBranchIgnoreCase("github.com", "main").size());
        assertEquals(1, gitRequestRepository.findByScmUrlIgnoreCaseAndScmBranchIgnoreCase("github.com", "dev").size());
    }
}
