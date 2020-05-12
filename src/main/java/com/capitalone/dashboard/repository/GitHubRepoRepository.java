package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.webhook.github.GitHubRepo;
import org.bson.types.ObjectId;

import java.util.List;

public interface GitHubRepoRepository extends BaseCollectorItemRepository<GitHubRepo> {
    default GitHubRepo findGitHubRepo(ObjectId collectorId, String url, String branch) {
        return findOne("collectorId eq " + quote(collectorId) + " and options.url equalsIgnoreCase " + quote(url) + " and options.branch equalsIgnoreCase " + quote(branch));
    }

    default List<GitHubRepo> findEnabledGitHubRepos(ObjectId collectorId) {
        return findAll("collectorId eq " + quote(collectorId) + " and enabled isTrue");
    }
}
