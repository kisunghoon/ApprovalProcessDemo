package com.salesforce.approvalprocessdemo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessSimpleResponse {

    private Long id;
    private String targetType;
    private Long targetId;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
}
