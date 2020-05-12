package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.ChangeOrder;

/**
 * Repository for {@link ChangeOrder} data.
 */
public interface ChangeOrderRepository extends QueryRepository<ChangeOrder> {
    ChangeOrder findByChangeID(String changeItem);
}
