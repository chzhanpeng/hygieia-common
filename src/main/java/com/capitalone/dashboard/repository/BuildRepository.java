package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildStatus;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for {@link Build} data.
 */
public interface BuildRepository extends QueryRepository<Build> {

    /**
     * Finds the {@link Build} with the given number for a specific {@link com.capitalone.dashboard.model.CollectorItem}.
     *
     * @param collectorItemId collector item id
     * @param number buld number
     * @return a {@link Build}
     */
    Build findByCollectorItemIdAndNumber(ObjectId collectorItemId, String number);

    default List<Build> findBuildsForRevisionNumbersAndBuildCollectorItemIds(List<String> scmRevisionNumbers, List<ObjectId> buildCollectorItemId) {
        String query = "sourceChangeSet.scmRevisionNumber isNotNull";
        query = and(query) + "sourceChangeSet.scmRevisionNumber in " + in(scmRevisionNumbers);
        query = and(query) + "collectorItemId in " + in(buildCollectorItemId.stream().map(ObjectId::toHexString).collect(Collectors.toList()));
        return findAll(query);
    }

    Build findTop1ByCollectorItemIdOrderByTimestampDesc(ObjectId collectorItemId);

    Build findTop1ByCollectorItemIdAndBuildStatusOrderByTimestampDesc(ObjectId collectorItemId, BuildStatus buildStatus);

    Build findTop1ByCollectorItemIdAndTimestampIsBetweenOrderByTimestampDesc(ObjectId collectorItemId, long beginDate, long endDate);
}
