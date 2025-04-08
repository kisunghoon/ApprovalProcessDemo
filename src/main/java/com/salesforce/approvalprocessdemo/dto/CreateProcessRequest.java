package com.salesforce.approvalprocessdemo.dto;

import com.salesforce.approvalprocessdemo.type.ApprovalType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateProcessRequest {

    private String targetType;
    private Long targetId;
    private String createdBy;

    private List<ApprovalNodeDto> nodes;

    @Data
    public static class ApprovalNodeDto {
        private ApprovalType approvalType;
        private List<Long> userIds;
    }
}
