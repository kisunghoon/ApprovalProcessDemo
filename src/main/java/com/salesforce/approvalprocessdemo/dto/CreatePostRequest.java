package com.salesforce.approvalprocessdemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CreatePostRequest {
    private String title;
    private String content;
}
