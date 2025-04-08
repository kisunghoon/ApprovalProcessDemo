package com.salesforce.approvalprocessdemo.repository;

import com.salesforce.approvalprocessdemo.domain.ApprovalTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalTemplateRepository extends JpaRepository<ApprovalTemplate,Long> {
}
