package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.config.MongoServerConfig;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = { MongoServerConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public abstract class MongoServerBaseRepositoryTest {

}
