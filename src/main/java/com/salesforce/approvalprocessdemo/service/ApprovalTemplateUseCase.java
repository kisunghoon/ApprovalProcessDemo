package com.salesforce.approvalprocessdemo.service;

import com.salesforce.approvalprocessdemo.domain.ApprovalTemplate;
import com.salesforce.approvalprocessdemo.dto.ApprovalTemplateResponse;
import com.salesforce.approvalprocessdemo.dto.CreateApprovalTemplateRequest;

import java.util.List;

public interface ApprovalTemplateUseCase {

    ApprovalTemplate createTemplate(CreateApprovalTemplateRequest request);
    List<ApprovalTemplateResponse> getAllTemplate();
}
