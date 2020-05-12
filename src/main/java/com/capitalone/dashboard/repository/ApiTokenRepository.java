package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.ApiToken;

import java.util.List;

public interface ApiTokenRepository extends QueryRepository<ApiToken> {
    ApiToken findByApiUserAndExpirationDt(String apiUser, Long expirationDt);
    ApiToken findByApiUserAndApiKey(String apiUser, String apiKey);
    List<ApiToken> findByApiUser(String apiUser);
}
