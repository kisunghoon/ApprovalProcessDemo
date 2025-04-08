package com.salesforce.approvalprocessdemo.repository;

import com.salesforce.approvalprocessdemo.domain.ProcessInstanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessInstanceHistoryRepository extends JpaRepository<ProcessInstanceHistory, Integer> {
}
