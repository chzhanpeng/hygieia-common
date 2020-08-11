package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CodeQuality;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CodeQualityRepositoryTest extends MongoServerBaseRepositoryTest {

    @Autowired
    CodeQualityRepository codeQualityRepository;

    @Before
    public void clean() {
        codeQualityRepository.deleteAll();
    }

    @Test
    public void testFindByCollectorItemIdAndTimestamp() {
        ObjectId id1 = new ObjectId("111111111111111111111111");
        CodeQuality cq = new CodeQuality();
        cq.setCollectorItemId(id1);
        cq.setTimestamp(1000);
        cq.setName("code quality");
        codeQualityRepository.save(cq);

        assertEquals("code quality", codeQualityRepository.findByCollectorItemIdAndTimestamp(id1, 1000).getName());
    }

    public void testFindByNameAndVersion() {
        CodeQuality cq1 = new CodeQuality();
        cq1.setName("cq1");
        cq1.setVersion("v1");

        CodeQuality cq2 = new CodeQuality();
        cq2.setName("cq2");
        cq2.setVersion("v2");

        codeQualityRepository.save(Arrays.asList(cq1, cq2));

        assertEquals(1, codeQualityRepository.findByNameAndVersion("cq1", "v1").size());
        assertEquals(1, codeQualityRepository.findByNameAndVersion("cq2", "v2").size());
    }

}
