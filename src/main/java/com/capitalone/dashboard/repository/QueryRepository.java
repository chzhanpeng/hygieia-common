package com.capitalone.dashboard.repository;


import com.capitalone.dashboard.query.model.QueryException;
import com.capitalone.dashboard.query.repository.OneQueryExecutor;
import org.apache.commons.collections4.IterableUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@NoRepositoryBean
public interface QueryRepository<T> extends OneQueryExecutor<T, ObjectId> {
    Logger LOGGER = LoggerFactory.getLogger(QueryRepository.class);


    default List<T> findAll (String query) {
        try {
            return IterableUtils.toList(find(query));
        } catch (QueryException qe) {
            LOGGER.error(qe.getMessage(), qe);
            return Collections.emptyList();
        }
    }

    default Page<T> findAllAsPage (String query) {
        try {
            return (Page<T>) find(query);
        } catch (QueryException qe) {
            LOGGER.error(qe.getMessage(), qe);
            return (Page<T>) IterableUtils.emptyIterable();
        }
    }

    @Override
    default T findOne (String query) {
        try {
            Iterable<T> items = find(query);
            if (IterableUtils.isEmpty(items)) {
                return null;
            } else {
                return IterableUtils.toList(items).get(0);
            }
        } catch (QueryException qe) {
            LOGGER.error(qe.getMessage(), qe);
            return null;
        }
    }

    default T findOne (ObjectId id) {
        return findOne("id eq " + quote(id.toHexString()));
    }

    default Iterable<T> save (Collection<T> documents) {
        return saveAll(documents);
    }

    default Iterable<T> save (Iterable<T> documents) {
        return saveAll(documents);
    }

    default void delete (Collection<T> documents) {
        deleteAll(documents);
    }

    default void delete (Iterable<T> documents) {
        deleteAll(documents);
    }
}
