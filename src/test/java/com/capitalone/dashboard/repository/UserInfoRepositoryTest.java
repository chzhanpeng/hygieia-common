package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.AuthType;
import com.capitalone.dashboard.model.UserInfo;
import com.capitalone.dashboard.model.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class UserInfoRepositoryTest extends MongoServerBaseRepositoryTest {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Before
    public void clean() {
        userInfoRepository.deleteAll();
    }

    @Test
    public void testFindUsernameAndAuthType() {
        UserInfo user1 = new UserInfo();
        user1.setUsername("user1");
        user1.setAuthType(AuthType.LDAP);
        userInfoRepository.save(user1);

        assertNotNull(userInfoRepository.findByUsernameAndAuthType("user1", AuthType.LDAP));
        assertNull(userInfoRepository.findByUsernameAndAuthType("user1", AuthType.STANDARD));
    }

    @Test
    public void testFindUsername() {
        UserInfo user1 = new UserInfo();
        user1.setUsername("user1");
        userInfoRepository.save(user1);

        assertNotNull(userInfoRepository.findByUsername("user1"));
        assertNull(userInfoRepository.findByUsername("user2"));
    }

    @Test
    public void testFindByOrderByUsernameAsc() {
        UserInfo user1 = new UserInfo();
        user1.setUsername("user3");
        userInfoRepository.save(user1);
        UserInfo user2 = new UserInfo();
        user2.setUsername("user2");
        userInfoRepository.save(Arrays.asList(user1, user2));

        Iterator<UserInfo> user = userInfoRepository.findByOrderByUsernameAsc().iterator();
        assertEquals("user2", user.next().getUsername());
        assertEquals("user3", user.next().getUsername());
    }

    @Test
    public void testFindByAuthRole() {
        UserInfo user1 = new UserInfo();
        user1.setUsername("user1");
        user1.setAuthorities(Arrays.asList(UserRole.ROLE_ADMIN, UserRole.ROLE_API));
        UserInfo user2 = new UserInfo();
        user2.setUsername("user2");
        user2.setAuthorities(Arrays.asList(UserRole.ROLE_USER, UserRole.ROLE_API));
        userInfoRepository.save(Arrays.asList(user1, user2));

        assertEquals(2, userInfoRepository.findByAuthoritiesIn(UserRole.ROLE_API).size());
        assertEquals(1, userInfoRepository.findByAuthoritiesIn(UserRole.ROLE_ADMIN).size());
        assertEquals(1, userInfoRepository.findByAuthoritiesIn(UserRole.ROLE_USER).size());
    }
}
