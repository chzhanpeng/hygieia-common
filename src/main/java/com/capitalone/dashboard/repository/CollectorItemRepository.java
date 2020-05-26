package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.util.GitHubParsedUrl;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link CollectorItem} repository
 */
public interface CollectorItemRepository extends BaseCollectorItemRepository<CollectorItem> {
    Logger LOGGER = LoggerFactory.getLogger(CollectorItemRepository.class);


    default List<CollectorItem> findCollectorItemsBySubsetOptions(ObjectId id, Map<String, Object> allOptions, Map<String, Object> uniqueOptions, Map<String, Object> uniqueOptionsFromCollector) {
        String query = String.format("collectorId eq '%s' ", id);
        uniqueOptions.values().removeIf(d -> d == null || ((d instanceof String) && StringUtils.isEmpty((String) d)));

        for (Map.Entry<String, Object> e : allOptions.entrySet()) {
            if (uniqueOptionsFromCollector.containsKey(e.getKey())) {
                query = and(query) + getQuery(uniqueOptions, e);
            }
        }

        return findAll(query);
    }

    default CollectorItem findTeamDashboardCollectorItemsByCollectorIdAndDashboardId(ObjectId collectorId, String dashboardId) {
        return findOne("collectorId eq " + quote(collectorId.toHexString() + " and options.dashboardId eq " + quote(dashboardId)));
    }

    default List<CollectorItem> findByOptionsAndDeployedApplicationName(ObjectId collectorId, String applicationName) {
        return findAll("collectorId eq " + quote(collectorId.toHexString()) + " and options.applicationName eq " + quote(applicationName));

    }

    default CollectorItem findByCollectorIdNiceNameAndJobName(ObjectId collectorId, String niceName, String jobName) {
        return findOne("collectorId eq " + quote(collectorId.toHexString()) + " and options.jobName eq " + quote(jobName) + " and niceName eq " + quote(niceName));
    }

    default CollectorItem findByCollectorIdNiceNameAndProjectId(ObjectId collectorId, String niceName, String projectId) {
        return findOne("collectorId eq " + quote(collectorId.toHexString()) + " and options.projectId eq " + quote(projectId) + " and niceName eq " + quote(niceName));
    }

    default CollectorItem findRepoByUrlAndBranch(ObjectId collectorId, String url, String branch, boolean enabled) {
        return findOne("collectorId eq " + quote(collectorId.toHexString()) + " and options.url startsWithIgnoreCase " + quote(url) + " and options.branch startsWithIgnoreCase " +
                quote(branch) + " and enabled eq " + enabled);
    }

    default CollectorItem findRepoByUrlAndBranch(ObjectId collectorId, String url, String branch) {
        return findOne("collectorId eq " + quote(collectorId.toHexString()) + " and options.url startsWithIgnoreCase " + quote(url) + " and options.branch startsWithIgnoreCase " +
                quote(branch));
    }

    default Page<CollectorItem> findByCollectorIdAndSearchField(List<ObjectId> collectorIds, String searchField, String searchFieldValue, Pageable pageable) {
        String query = "collectorId in " + in(collectorIds.stream().map(ObjectId::toHexString).collect(Collectors.toList()))
                + " and " + searchField + " containsIgnoreCase " + quote(searchFieldValue);
        return findAllAsPage( page(query, pageable));
    }

    default Page<CollectorItem> findByCollectorIdAndSearchFields(List<ObjectId> collectorIds, String searchField1, String searchFieldValue1, String searchField2, String searchFieldValue2, Pageable pageable) {
        String query = "collectorId in " + in(collectorIds.stream().map(ObjectId::toHexString).collect(Collectors.toList()))
                + " and " + searchField1 + " containsIgnoreCase " + quote(searchFieldValue1)
                + " and " + searchField2 + " containsIgnoreCase " + quote(searchFieldValue2);
        return findAllAsPage( page(query, pageable));
    }

    default CollectorItem findByJiraTeamId(String teamId) {
        return findOne("options.teamId eq " + quote(teamId));
    }

    default CollectorItem findByJiraProjectId(String projectId) {
        return findOne("options.teamId eq " + quote(projectId));
    }

    default List<CollectorItem> findByDescription(String description) { return findAll("description eq " + quote(description)); }

    default List<CollectorItem> findByArtifactNameAndPath(String artifactName, String path) {
        return findAll(("options.artifactName eq " + quote(artifactName) + " and options.path eq " + quote(path)));
    }

    default Iterable<CollectorItem> findAllByOptionNameValue(String optionName, String optionValue) {
        try {
            return find(String.format("option.%s eq %s", optionName, optionValue));
        } catch (QueryException qe) {
            LOGGER.error(qe.getMessage(), qe);
            return IterableUtils.emptyIterable();
        }
    }

    default Iterable<CollectorItem> findAllByOptionNameValueAndCollectorIdsIn(String optionName, String optionValue, List<ObjectId> collectorIds) {
        Map<String, Object> inMap = new HashMap<>();
        inMap.put(optionName, optionValue);
        return findAllByOptionMapAndCollectorIdsIn(inMap, collectorIds);
    }

    default Iterable<CollectorItem> findAllByOptionMapAndCollectorIdsIn(Map<String, Object> options, List<ObjectId> collectorIds) {
        String query = "";
        for (Map.Entry<String, Object> stringObjectEntry : options.entrySet()) {
            query = and(query) + formatEntry(stringObjectEntry);
        }
        query = and(query) + "collectorId in " + in(collectorIds.stream().map(ObjectId::toHexString).collect(Collectors.toList()));
        try {
            return find(query);
        } catch (QueryException qe) {
            LOGGER.error(qe.getMessage(), qe);
            return IterableUtils.emptyIterable();
        }
    }

    default String formatEntry(Map.Entry<String, Object> entry) {
        String template = "options.%s eq %s";
        return String.format(template, entry.getKey(), (entry.getValue() instanceof Number) ? entry.getValue() : quote(entry.getValue()));
    }

    default String getGitHubParsedString(Map<String, Object> options, Map.Entry<String, Object> e) {
        String url = (String) options.get(e.getKey());
        GitHubParsedUrl gitHubParsedUrl = new GitHubParsedUrl(url);
        return gitHubParsedUrl.getUrl();
    }

    default String getQuery(Map<String, Object> options, Map.Entry<String, Object> e) {
        if ("url".equalsIgnoreCase(e.getKey())) {
            String url = getGitHubParsedString(options, e);
            return "options." + e.getKey() + " equalsIgnoreCase " + quote(url);
        } else if ("branch".equalsIgnoreCase(e.getKey())) {
            String branch = (String) options.get(e.getKey());
            return "options." + e.getKey() + " equalsIgnoreCase " + quote(branch);
        } else {
            return "options." + e.getKey() + " eq " + quote(options.get(e.getKey()));
        }
    }

}
