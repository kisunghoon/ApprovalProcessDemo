package com.salesforce.approvalprocessdemo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ApprovalTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ApprovalTemplateId;

    private String name;
    private String targetType;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ApprovalTemplateNode> nodes = new ArrayList<>();
}
