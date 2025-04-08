package com.salesforce.approvalprocessdemo.domain;

import com.salesforce.approvalprocessdemo.type.WorkItemStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
/*
특정 승인자의 승인 작업. 승인 여부 및 코멘트 등을 포함.
특정 사용자에게 할당된 승인 작업 항목입니다.
 */
@Entity
@Getter
@Setter
public class ProcessInstanceWorkItem {

    @Id
    @GeneratedValue
    private Long processInstanceWorkItemId;

    @ManyToOne
    private ProcessInstance processInstance; //소속된 승인 요청

    @ManyToOne
    private ProcessInstanceNode node; // 소속된 승인 단계

    private Long userId; // 승인 담당자의 ID

    @Enumerated(EnumType.STRING)
    private WorkItemStatus status; // 현재 작업 상태

    private String comment; // 승인자가 남긴 코멘트
    private LocalDateTime actedAt; // 승인 또는 반려가 처리된 시각
}
