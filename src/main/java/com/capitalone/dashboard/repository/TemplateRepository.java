package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.Template;
import org.bson.types.ObjectId;

/**
 * {@link Template} repository.
 */
public interface TemplateRepository extends QueryRepository<Template> {

    Template findByTemplate(String template);

    void deleteById(ObjectId id);

    default void delete(ObjectId id) {
        deleteById(id);
    }
}
