package com.salesforce.approvalprocessdemo.repository;

import com.salesforce.approvalprocessdemo.domain.ProcessInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessInstanceRepository extends JpaRepository<ProcessInstance, Long> {

    Optional<ProcessInstance> findByProcessInstanceId(Long processInstanceId);
}
