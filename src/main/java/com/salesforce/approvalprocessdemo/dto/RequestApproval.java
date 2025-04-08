package com.salesforce.approvalprocessdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestApproval {

    private Long templateId;
    private String createdBy;
}
