package com.salesforce.approvalprocessdemo.repository;

import com.salesforce.approvalprocessdemo.domain.ProcessInstanceWorkItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessInstanceWorkItemRepository extends JpaRepository<ProcessInstanceWorkItem, Long> {
}
