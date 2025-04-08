package com.salesforce.approvalprocessdemo.service;

import com.salesforce.approvalprocessdemo.domain.ProcessInstance;
import com.salesforce.approvalprocessdemo.dto.*;

import java.util.List;

public interface ProcessUseCase {

    /**
     * 승인 요청 생성
     * @param request
     * @return
     */
    ProcessResponse createProcess(CreateProcessRequest request);

    ProcessDetailResponse getDetail(Long processInstanceId);

    List<ProcessSimpleResponse> getAllProcess();

    List<StepResponse> getSteps(Long processInstanceId);

    void approve(Long workItemId, Long userId);

    void reject(Long workItemId, Long userId);

    ProcessInstance createProcessFromTemplate(Long templateId, String targetType, Long targetId, String createdBy);
}
