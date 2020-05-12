package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CodeReposBuilds;

/**
 * Repository for {@link CodeReposBuilds} data.
 */
public interface CodeReposBuildsRepository extends QueryRepository<CodeReposBuilds> {

    CodeReposBuilds findByCodeRepo(String codeRepo);

}
