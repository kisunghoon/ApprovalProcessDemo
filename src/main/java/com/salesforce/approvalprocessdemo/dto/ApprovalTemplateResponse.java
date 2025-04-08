package com.salesforce.approvalprocessdemo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApprovalTemplateResponse {

    private Long id;
    private String name;
    private List<NodeDto> nodes;

    @Setter
    @Getter
    public static class NodeDto{
        private int order;
        private String approvalType;
        private List<Long> approverIDS;
    }
}
