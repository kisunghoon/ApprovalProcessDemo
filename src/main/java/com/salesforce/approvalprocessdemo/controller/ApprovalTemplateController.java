package com.salesforce.approvalprocessdemo.controller;

import com.salesforce.approvalprocessdemo.dto.ApprovalTemplateResponse;
import com.salesforce.approvalprocessdemo.dto.CreateApprovalTemplateRequest;
import com.salesforce.approvalprocessdemo.service.ApprovalTemplateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class ApprovalTemplateController {

    private final ApprovalTemplateUseCase approvalTemplateUseCase;

    @PostMapping
    public ResponseEntity<Void> createTemplate(@RequestBody CreateApprovalTemplateRequest request){
        approvalTemplateUseCase.createTemplate(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ApprovalTemplateResponse>> getTemplates(){
        return ResponseEntity.ok(approvalTemplateUseCase.getAllTemplate());
    }



}
