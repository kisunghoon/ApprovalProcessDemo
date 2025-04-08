package com.salesforce.approvalprocessdemo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {

    private Long postId;
    private String titile;
    private String content;
    private String status;
    private Long processInstanceId;
}
