package com.salesforce.approvalprocessdemo.dto;

import com.salesforce.approvalprocessdemo.type.ApprovalType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProcessDetailResponse {

    private Long id;
    private String targetType;
    private Long targetId;
    private String status;
    private String createdBy;
    private LocalDateTime createdAt;

    private List<NodeDto> nodes;

    @Data
    @Builder
    public static class NodeDto {
        private int order;
        private ApprovalType approvalType;
        private List<ApproverDto> approvers;
    }

    @Data
    @Builder
    public static class ApproverDto {
        private Long userId;
        private String status;
        private String comment;
        private LocalDateTime actedAt;
    }
}
