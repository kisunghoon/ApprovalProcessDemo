package com.salesforce.approvalprocessdemo.domain;

import com.salesforce.approvalprocessdemo.type.ApprovalType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 승인 단계.
 * 노드마다 여러 승인자(Workitem)를 가질 수 있음.
 * 단계 순서 제어에 사용됨.
 * 하나의 승인 요청(ProcessInstance) 안에서 단계적 승인 흐름을 표현하는 구조입니다
 */

@Entity
@Getter
@Setter
public class ProcessInstanceNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processInstanceNodeId;

    @ManyToOne
    private ProcessInstance processInstance; // 소속된 승인 요청
    private Integer sequenceOrder; // 단계 순서

    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType; //승인 방식 (순차 or 병렬)

    @OneToMany(mappedBy = "node" , cascade = CascadeType.ALL)
    private List<ProcessInstanceWorkItem> workItems = new ArrayList<>(); // 승인자들이 승인해야 할 작업 항목 리스트

    public void addWorkItem(ProcessInstanceWorkItem workItem) {
        this.workItems.add(workItem);
        workItem.setNode(this);

    }
}
