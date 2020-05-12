package com.capitalone.dashboard.repository;

import com.capitalone.dashboard.model.CloudInstanceHistory;

import java.util.Collection;

public interface CloudInstanceHistoryRepository extends QueryRepository<CloudInstanceHistory> {

    Collection<CloudInstanceHistory> findByAccountNumber(String accountNumber);

}
