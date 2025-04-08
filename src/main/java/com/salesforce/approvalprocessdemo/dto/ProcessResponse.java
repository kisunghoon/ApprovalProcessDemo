package com.salesforce.approvalprocessdemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessResponse {
    private Long processInstanceId;
    private String status;
}
