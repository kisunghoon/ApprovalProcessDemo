package com.salesforce.approvalprocessdemo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ProcessInstanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processInstanceHistoryId;

    @ManyToOne
    private ProcessInstance processInstance; //소속된 승인 요청

    private Long workItemId;
    private Long userId;
    private String action;
    private String comment;
    private LocalDateTime actedAt;
}
