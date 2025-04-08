package com.salesforce.approvalprocessdemo.repository;

import com.salesforce.approvalprocessdemo.domain.ProcessInstanceStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessInstanceStepRepository extends JpaRepository<ProcessInstanceStep, Long> {
}
