package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.GenericCollectorItem;
import org.bson.types.ObjectId;

import java.util.List;

/**
 */
public interface GenericCollectorItemRepository extends QueryRepository<GenericCollectorItem> {
    List<GenericCollectorItem> findAllByToolName(String toolName);
    List<GenericCollectorItem> findAllByRelatedCollectorItem (ObjectId relatedCollectorItemId);
    GenericCollectorItem findByToolNameAndRawDataAndRelatedCollectorItem(String toolName, String rawData, ObjectId relatedCollectorItem);
    List<GenericCollectorItem> findAllByToolNameAndProcessTimeEquals(String toolName, long processTime);
}
