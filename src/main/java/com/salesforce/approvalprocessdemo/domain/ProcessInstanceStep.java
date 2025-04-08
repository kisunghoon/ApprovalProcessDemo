package com.salesforce.approvalprocessdemo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 승인 단계에서 발생한 상태 변화의 기록.
 * 승인/거절 등 특정 action 결과로 기록.
 * 승인자가 승인/거절 등의 액션을 취할 때마다 기록을 남기는 객체입니다.
 */
@Entity
@Getter
@Setter
public class ProcessInstanceStep {

    @Id
    @GeneratedValue
    private Long processInstanceStepId;

    @ManyToOne
    private ProcessInstance processInstance; // 소속된 승인 요청

    @ManyToOne
    private ProcessInstanceNode node; // 실제 실행된 승인 단계

    private Integer sequenceOrder;

    private LocalDateTime completedAt;

    private String result;
}
