package com.salesforce.approvalprocessdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salesforce.approvalprocessdemo.type.ApproveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private ApproveStatus status;

    @OneToOne
    @JoinColumn(name = "process_instance_id")
    private ProcessInstance processInstance;
}
