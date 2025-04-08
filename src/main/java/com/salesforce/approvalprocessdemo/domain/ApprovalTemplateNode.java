package com.salesforce.approvalprocessdemo.domain;


import com.salesforce.approvalprocessdemo.type.ApprovalType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class ApprovalTemplateNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalTemplateNodeId;

    private int seqOrder;

    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType;

    @ElementCollection
    private List<Long> userIds;

    @ManyToOne
    private ApprovalTemplate template;

    @OneToMany(mappedBy = "node" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalTemplateApprover> approvers = new ArrayList<>();

    public void addApprover(ApprovalTemplateApprover approver){
        this.approvers.add(approver);
        approver.setNode(this);
    }


}
