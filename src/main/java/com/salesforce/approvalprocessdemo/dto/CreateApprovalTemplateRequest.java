package com.salesforce.approvalprocessdemo.dto;

import com.salesforce.approvalprocessdemo.type.ApprovalType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateApprovalTemplateRequest {

    private String name;
    private String targetType;

    private List<NodeDto> nodes;

    @Getter
    @Setter
    @Builder
    public static class NodeDto {
        private Integer order;
        private ApprovalType approvalType;
        private List<Long> userIds;
    }
}
