package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Authentication;

public interface AuthenticationRepository extends QueryRepository<Authentication>{
	 Authentication findByUsername(String username);
}
