package com.salesforce.approvalprocessdemo.repository;

import com.salesforce.approvalprocessdemo.domain.Post;
import com.salesforce.approvalprocessdemo.domain.ProcessInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByProcessInstance(ProcessInstance processInstance);
}
