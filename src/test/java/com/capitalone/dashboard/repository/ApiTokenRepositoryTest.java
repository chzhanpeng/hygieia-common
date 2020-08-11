package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.ApiToken;
import com.google.common.hash.Hashing;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

public class ApiTokenRepositoryTest extends MongoServerBaseRepositoryTest {

    @Autowired
    ApiTokenRepository apiTokenRepository;

    @Before
    public void clean() {
        apiTokenRepository.deleteAll();
    }

    @Test
    public void testFindByApiUserAndExpirationDt() {
        ApiToken token = new ApiToken("user", "key", (long) 100000);
        apiTokenRepository.save(token);
        assertTrue(apiTokenRepository.findByApiUserAndExpirationDt("user", (long) 100000).checkApiKey("key"));
    }

    @Test
    public void testFindByApiUserAndApiKey() {
        ApiToken token = new ApiToken("user", "key", (long) 100000);
        apiTokenRepository.save(token);
        assertTrue(apiTokenRepository.findByApiUserAndApiKey("user", hash("key")).checkApiKey("key"));
    }

    @Test
    public void testFindByApiUser() {
        ApiToken token = new ApiToken("user", "key", (long) 100000);
        apiTokenRepository.save(token);
        assertTrue(apiTokenRepository.findByApiUser("user").get(0).checkApiKey("key"));
    }

    private String hash(String apiKey) {
        String HASH_PREFIX = "sha512:";
        if (!apiKey.startsWith(HASH_PREFIX)) {
            return HASH_PREFIX + Hashing.sha512().hashString(apiKey, StandardCharsets.UTF_8).toString();
        }
        return apiKey;
    }}
