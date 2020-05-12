package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.AuthType;
import com.capitalone.dashboard.model.UserInfo;
import com.capitalone.dashboard.model.UserRole;

import java.util.Collection;

public interface UserInfoRepository extends QueryRepository<UserInfo>{

	UserInfo findByUsernameAndAuthType(String username, AuthType authType);

    Collection<UserInfo> findByAuthoritiesIn(UserRole roleAdmin);

    Iterable<UserInfo> findByOrderByUsernameAsc();

    UserInfo findByUsername(String userName);

}
