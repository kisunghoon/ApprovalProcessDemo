package com.salesforce.approvalprocessdemo.service.impl;

import com.salesforce.approvalprocessdemo.domain.ApprovalTemplate;
import com.salesforce.approvalprocessdemo.domain.ApprovalTemplateApprover;
import com.salesforce.approvalprocessdemo.domain.ApprovalTemplateNode;
import com.salesforce.approvalprocessdemo.dto.ApprovalTemplateResponse;
import com.salesforce.approvalprocessdemo.dto.CreateApprovalTemplateRequest;
import com.salesforce.approvalprocessdemo.repository.ApprovalTemplateRepository;
import com.salesforce.approvalprocessdemo.service.ApprovalTemplateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalTemplateService implements ApprovalTemplateUseCase {

    private final ApprovalTemplateRepository approvalTemplateRepository;

    @Override
    public ApprovalTemplate createTemplate(CreateApprovalTemplateRequest request) {

        ApprovalTemplate template = new ApprovalTemplate();
        template.setName(request.getName());
        template.setTargetType(request.getTargetType());

        List<ApprovalTemplateNode> nodes = new ArrayList<>();

        for(CreateApprovalTemplateRequest.NodeDto nodeDto : request.getNodes()){
            ApprovalTemplateNode node = new ApprovalTemplateNode();

            node.setSeqOrder(nodeDto.getOrder());
            node.setApprovalType(nodeDto.getApprovalType());
            node.setTemplate(template);

            nodeDto.getUserIds().forEach(userId -> {
                ApprovalTemplateApprover approver = new ApprovalTemplateApprover();
                approver.setUserID(userId);
                approver.setNode(node);
                node.addApprover(approver);
            });

            nodes.add(node);
        }

        template.setNodes(nodes);
        return approvalTemplateRepository.save(template);
    }

    @Override
    public List<ApprovalTemplateResponse> getAllTemplate() {

        List<ApprovalTemplate> templates = approvalTemplateRepository.findAll();

        return templates.stream().map(template ->{
            ApprovalTemplateResponse response = new ApprovalTemplateResponse();

            response.setId(template.getApprovalTemplateId());
            response.setName(template.getName());

            List<ApprovalTemplateResponse.NodeDto> nodeDtos = template.getNodes().stream()
                    .map(node ->{
                        ApprovalTemplateResponse.NodeDto nodeDto = new ApprovalTemplateResponse.NodeDto();
                        nodeDto.setOrder(node.getSeqOrder());
                        nodeDto.setApprovalType(node.getApprovalType().name());

                        List<Long> approverIds = node.getApprovers().stream()
                                .map(approver -> approver.getUserID()).toList();

                        nodeDto.setApproverIDS(approverIds);
                        return nodeDto;
                    }).toList();

            response.setNodes(nodeDtos);
            return response;
        }).toList();
    }
}
