package com.salesforce.approvalprocessdemo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ApprovalTemplateApprover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalTemplateApproverId;

    private Long userID;

    @ManyToOne
    private ApprovalTemplateNode node;
}
