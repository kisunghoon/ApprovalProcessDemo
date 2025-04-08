package com.salesforce.approvalprocessdemo.service.impl;

import com.salesforce.approvalprocessdemo.domain.*;
import com.salesforce.approvalprocessdemo.dto.*;
import com.salesforce.approvalprocessdemo.repository.*;
import com.salesforce.approvalprocessdemo.service.ProcessUseCase;
import com.salesforce.approvalprocessdemo.type.ApprovalType;
import com.salesforce.approvalprocessdemo.type.ApproveStatus;
import com.salesforce.approvalprocessdemo.type.WorkItemStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProcessService implements ProcessUseCase {

    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessInstanceWorkItemRepository processInstanceWorkItemRepository;
    private final UserRepository userRepository;
    private final ProcessInstanceStepRepository processInstanceStepRepository;
    private final ApprovalTemplateRepository approvalTemplateRepository;
    private final PostRepository postRepository;

    /*
    * 승인 요청 생성
    * */
    @Override
    public ProcessResponse createProcess(CreateProcessRequest request) {

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setTargetType(request.getTargetType());
        processInstance.setTargetId(request.getTargetId());
        processInstance.setStatus("PENDING");
        processInstance.setCreatedBy(request.getCreatedBy());
        processInstance.setCreatedAt(LocalDateTime.now());

        //승인 단계 생성
        List<ProcessInstanceNode> nodes = createNodes(request.getNodes(),processInstance);
        processInstance.setNodes(nodes);

        processInstanceRepository.save(processInstance);

        return ProcessResponse.builder()
                .processInstanceId(processInstance.getProcessInstanceId())
                .status(processInstance.getStatus())
                .build();
    }

    /*
    * 승인 단계 생성
    * */
    private List<ProcessInstanceNode> createNodes(List<CreateProcessRequest.ApprovalNodeDto> nodeDtos, ProcessInstance instance) {

        List<ProcessInstanceNode> nodes = new ArrayList<>();

        for(int i=0;i < nodeDtos.size();i++) {
            CreateProcessRequest.ApprovalNodeDto nodeDto = nodeDtos.get(i);

            ProcessInstanceNode node = new ProcessInstanceNode();
            node.setSequenceOrder(i);
            node.setApprovalType(nodeDto.getApprovalType());
            node.setProcessInstance(instance);

            List<User> users = userRepository.findAllById(nodeDto.getUserIds());


            // 각 단계별 승인자 등록
            createWorkItems(users,node,instance);

            nodes.add(node);
        }

        return nodes;
    }

    /*
    * 승인 단계에서 승인자 등록
    * */
    private void createWorkItems(List<User> users,ProcessInstanceNode node,ProcessInstance instance) {

        List<ProcessInstanceWorkItem> workItems = new ArrayList<>();

        for(User user : users){
            ProcessInstanceWorkItem workItem = new ProcessInstanceWorkItem();
            workItem.setProcessInstance(instance);
            workItem.setUserId(user.getUserId());
            workItem.setStatus(WorkItemStatus.WAITING);

            workItem.setProcessInstance(instance);
            node.addWorkItem(workItem);
            instance.getWorkItems().add(workItem);
        }
    }

    @Override
    public ProcessDetailResponse getDetail(Long processInstanceId) {

        ProcessInstance instance = processInstanceRepository.findByProcessInstanceId(processInstanceId)
                .orElseThrow(() -> new RuntimeException("해당 프로세스를 찾을 수 없습니다."));

        List<ProcessDetailResponse.NodeDto> nodeDtos = instance.getNodes().stream()
                .map(node -> ProcessDetailResponse.NodeDto.builder()
                        .order(node.getSequenceOrder())
                        .approvalType(node.getApprovalType())
                        .approvers(node.getWorkItems().stream()
                                .map(item -> ProcessDetailResponse.ApproverDto.builder()
                                            .userId(item.getUserId())
                                            .status(item.getStatus().name())
                                            .comment(item.getComment())
                                            .actedAt(item.getActedAt())
                                            .build()).toList())
                        .build()).toList();

        return ProcessDetailResponse.builder()
                .id(instance.getProcessInstanceId())
                .targetType(instance.getTargetType())
                .targetId(instance.getTargetId())
                .status(instance.getStatus())
                .createdBy(instance.getCreatedBy())
                .createdAt(instance.getCreatedAt())
                .nodes(nodeDtos)
                .build();
    }

    @Override
    public List<ProcessSimpleResponse> getAllProcess() {

        return processInstanceRepository.findAll().stream()
                .map(instance -> new ProcessSimpleResponse(
                        instance.getProcessInstanceId(),
                        instance.getTargetType(),
                        instance.getTargetId(),
                        instance.getStatus(),
                        instance.getCreatedAt(),
                        instance.getCreatedBy()

                )).toList();
    }

    @Override
    public List<StepResponse> getSteps(Long processInstanceId) {

        ProcessInstance instance = processInstanceRepository.findByProcessInstanceId(processInstanceId)
                .orElseThrow(() -> new RuntimeException("해당 프로세스를 찾을 수 없습니다."));


        return instance.getSteps().stream()
                .map(step-> StepResponse.builder()
                        .sequenceOrder(step.getSequenceOrder())
                        .result(step.getResult())
                        .completedAt(step.getCompletedAt())
                        .build()).toList();
    }

    @Override
    public void approve(Long workItemId,Long userId) {

        ProcessInstanceWorkItem item = processInstanceWorkItemRepository.findById(workItemId)
                        .orElseThrow(() -> new RuntimeException("WorkItem을 찾을 수 없습니다."));


        if(!item.getUserId().equals(userId)){
            throw new RuntimeException("이 사용자는 해당 승인 항목을 처리할 권한이 없습니다.");
        }

        if(!item.getStatus().equals(WorkItemStatus.WAITING)) {
            throw new RuntimeException("이미 처리된 항목입니다.");
        }

        ProcessInstanceNode node = item.getNode();
        ProcessInstance process = item.getProcessInstance();

        validApprove(process, node);

        item.setStatus(WorkItemStatus.APPROVED);
        item.setActedAt(LocalDateTime.now());

        Post post = postRepository.findByProcessInstance(process)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if (node.getApprovalType() == ApprovalType.SEQUENTIAL) {
            handleSequentialApproval(node, process);
        } else {
            handleParallelApproval(node, process);
        }
        post.setStatus(ApproveStatus.APPROVED);

        postRepository.save(post);
    }

    @Override
    public void reject(Long workItemId, Long userId) {
        ProcessInstanceWorkItem item = processInstanceWorkItemRepository.findById(workItemId)
                .orElseThrow(() -> new RuntimeException("WorkItem을 찾을 수 없습니다."));

        if(!item.getStatus().equals(WorkItemStatus.WAITING)) {
            throw new RuntimeException("이미 처리된 항목입니다.");
        }

        item.setStatus(WorkItemStatus.REJECTED);

        ProcessInstance process = item.getProcessInstance();
        process.setStatus("REJECTED");

        Post post = postRepository.findByProcessInstance(process)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        recordStep(process,item.getNode().getSequenceOrder(),"REJECTED");

        post.setStatus(ApproveStatus.REJECTED);

        postRepository.save(post);
    }

    private void recordStep(ProcessInstance process, int sequenceOrder , String type){

        ProcessInstanceStep step = new ProcessInstanceStep();
        step.setProcessInstance(process);
        step.setSequenceOrder(sequenceOrder);
        step.setCompletedAt(LocalDateTime.now());
        step.setResult(type);

        process.getSteps().add(step);
        processInstanceStepRepository.save(step);
    }

    private void validApprove(ProcessInstance process , ProcessInstanceNode node){
        Optional<ProcessInstanceNode> curActiveNode = process.getNodes().stream()
                .filter(n -> n.getWorkItems().stream()
                        .anyMatch(i -> i.getStatus() == WorkItemStatus.WAITING))
                .findFirst();

        if(curActiveNode.isEmpty() || !curActiveNode.get().equals(node)){
            throw new RuntimeException("현재 승인 가능한 단계가 아닙니다.");
        }
    }

    private void handleSequentialApproval(ProcessInstanceNode node, ProcessInstance process) {

        int curOrder = node.getSequenceOrder();

        recordStep(process,curOrder,"APPROVED");

        Optional<ProcessInstanceNode> nextNode = process.getNodes().stream()
                .filter(n -> n.getSequenceOrder() == curOrder + 1).findFirst();

        if(nextNode.isPresent()) {
            nextNode.get().getWorkItems().forEach(i -> i.setStatus(WorkItemStatus.WAITING));
        } else {
            process.setStatus("COMPLETED");
        }
    }

    private void handleParallelApproval(ProcessInstanceNode node, ProcessInstance process) {

        boolean allApproved = node.getWorkItems().stream()
                .allMatch(item -> item.getStatus() == WorkItemStatus.APPROVED);

        if(allApproved){
            log.info("parra;;");
            int curOrder = node.getSequenceOrder();

            recordStep(process,curOrder,"APPROVED");

            Optional<ProcessInstanceNode> nextNode = process.getNodes().stream()
                    .filter(n -> n.getSequenceOrder() == curOrder + 1).findFirst();

            if(nextNode.isPresent()) {
                nextNode.get().getWorkItems().forEach(i -> i.setStatus(WorkItemStatus.WAITING));
            } else {
                process.setStatus("COMPLETED");
            }

        }
    }

    public ProcessInstance createProcessFromTemplate(Long templateId, String targetType, Long targetId, String createdBy) {

        ApprovalTemplate template = approvalTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("템플릿이 존재하지 않습니다."));
        log.info("createProcessFromTemplate ");
        ProcessInstance instance = new ProcessInstance();
        instance.setTargetId(targetId);
        instance.setTargetType(targetType);
        instance.setStatus(createdBy);
        instance.setCreatedAt(LocalDateTime.now());

        List<ProcessInstanceNode> nodes = new ArrayList<>();

        for(ApprovalTemplateNode aNode : template.getNodes()){

            ProcessInstanceNode node = new ProcessInstanceNode();
            node.setSequenceOrder(aNode.getSeqOrder());
            node.setApprovalType(aNode.getApprovalType());
            node.setProcessInstance(instance);

            List<ProcessInstanceWorkItem> workItems = new ArrayList<>();
            log.info("createProcessFromTemplate aNode.getUserIds() : {} ",aNode.getUserIds());

            for(ApprovalTemplateApprover approver : aNode.getApprovers()){
                log.info("userId : {}", approver.getUserID());

                ProcessInstanceWorkItem wi = new ProcessInstanceWorkItem();
                wi.setUserId(approver.getUserID());
                wi.setStatus(WorkItemStatus.WAITING);
                wi.setProcessInstance(instance);
                wi.setNode(node);
                workItems.add(wi);
            }

            node.setWorkItems(workItems);
            nodes.add(node);
        }
        instance.setNodes(nodes);
        instance.setWorkItems(nodes.stream().flatMap(n -> n.getWorkItems().stream()).toList());

        return instance;
    }
}
