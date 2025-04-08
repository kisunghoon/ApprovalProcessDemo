package com.salesforce.approvalprocessdemo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 승인 요청의 루트 객체.
 * 어떤 대상(target)에 대해 승인 요청이 들어왔는지와 상태를 관리.
 * 승인 요청 하나를 의미합니다.
 */

@Entity
@Getter
@Setter
public class ProcessInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processInstanceId;

    private String targetType; // 승인 대상 타입
    private Long targetId; // 실제 승인 대상 ID
    private LocalDateTime createdAt; // 생성 시작
    private String createdBy; // 생성한 사용자명
    private String status;

    // 승인 단계 리스트
    @OneToMany(mappedBy = "processInstance" , cascade = CascadeType.ALL)
    private List<ProcessInstanceNode> nodes = new ArrayList<>();

    //승인 요청에 대한 변경 로그
    @OneToMany(mappedBy = "processInstance" , cascade = CascadeType.ALL)
    private List<ProcessInstanceHistory> histories = new ArrayList<>();

    //승인 진행 중 각 단계별 완료 기록 (예: 각 단계별 완료 시간 저장 등)
    @OneToMany(mappedBy = "processInstance" , cascade = CascadeType.ALL)
    private List<ProcessInstanceStep> steps = new ArrayList<>();

    // 실제 승인자별 승인 작업 항목 리스트
    @OneToMany(mappedBy = "processInstance" , cascade =  CascadeType.ALL)
    private List<ProcessInstanceWorkItem> workItems = new ArrayList<>();

}
