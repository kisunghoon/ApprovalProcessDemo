package com.salesforce.approvalprocessdemo.controller;

import com.salesforce.approvalprocessdemo.dto.*;
import com.salesforce.approvalprocessdemo.service.ProcessUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessUseCase processUseCase;

    @PostMapping
    public ResponseEntity<ProcessResponse> createProcess(@RequestBody CreateProcessRequest request) {

        ProcessResponse response = processUseCase.createProcess(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProcessSimpleResponse>> getProcesses() {

        return ResponseEntity.ok(processUseCase.getAllProcess());
    }

    @GetMapping("/{processId}")
    public ResponseEntity<ProcessDetailResponse> getDetail(@PathVariable Long processId) {
        return ResponseEntity.ok(processUseCase.getDetail(processId));
    }

    @PostMapping("/work-items/{workItemId}/action")
    public ResponseEntity<Void> approveOrOReject(@PathVariable Long workItemId,
            @RequestParam("action") String action
            ,@RequestParam("userId")Long userId){

        if(action.equalsIgnoreCase("approve")){
            processUseCase.approve(workItemId , userId);
        }else if(action.equalsIgnoreCase("reject")){
            processUseCase.reject(workItemId , userId);
        } else {
            throw new IllegalArgumentException("Invalid action: " + action);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{processInstanceId}/steps")
    public ResponseEntity<List<StepResponse>> getSteps(@PathVariable Long processInstanceId) {
        List<StepResponse> steps = processUseCase.getSteps(processInstanceId);
        return ResponseEntity.ok(steps);
    }

}
