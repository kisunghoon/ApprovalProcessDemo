package com.salesforce.approvalprocessdemo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepResponse {

    private int sequenceOrder;
    private String result;
    private LocalDateTime completedAt;
}
