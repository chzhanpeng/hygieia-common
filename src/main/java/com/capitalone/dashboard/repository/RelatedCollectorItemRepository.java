package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.relation.RelatedCollectorItem;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Repository for {@link Build} data.
 */
public interface RelatedCollectorItemRepository extends QueryRepository<RelatedCollectorItem> {
    List<RelatedCollectorItem> findRelatedCollectorItemByLeft(ObjectId left);
    List<RelatedCollectorItem> findRelatedCollectorItemByRight(ObjectId right);
    List<RelatedCollectorItem> findAllByLeftAndRight(ObjectId left, ObjectId right);

    default RelatedCollectorItem saveRelatedItems(ObjectId left, ObjectId right, String source, String reason) {
        List<RelatedCollectorItem> items = findAllByLeftAndRight(left, right);
        if (!CollectionUtils.isEmpty(items)) {
            this.deleteAll(items);
        }
        RelatedCollectorItem related = new RelatedCollectorItem();
        related.setLeft(left);
        related.setRight(right);
        related.setCreationTime(System.currentTimeMillis());
        related.setSource(source);
        related.setReason(reason);
        return save(related);
    }
    List<RelatedCollectorItem> findAllByCreationTimeIsBetweenOrderByCreationTimeDesc(long beginDate, long endDate);
    List<RelatedCollectorItem> findAllByReasonAndCreationTimeIsBetweenOrderByCreationTimeDesc(String reason, long beginDate, long endDate);
}
