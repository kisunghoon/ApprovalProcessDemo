package com.salesforce.approvalprocessdemo.aop;

import com.salesforce.approvalprocessdemo.domain.ProcessInstance;
import com.salesforce.approvalprocessdemo.domain.ProcessInstanceHistory;
import com.salesforce.approvalprocessdemo.domain.ProcessInstanceWorkItem;
import com.salesforce.approvalprocessdemo.repository.ProcessInstanceHistoryRepository;
import com.salesforce.approvalprocessdemo.repository.ProcessInstanceWorkItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApprovalHistoryAspect {

    private final ProcessInstanceHistoryRepository historyRepository;
    private final ProcessInstanceWorkItemRepository workItemRepository;

    @AfterReturning(
            pointcut = "execution(* com.salesforce.approvalprocessdemo.service.ProcessUseCase.approve(..)) || " +
                    "execution(* com.salesforce.approvalprocessdemo.service.ProcessUseCase.reject(..))",
            returning = "result"
    )
    public void recordApprovalHistory(JoinPoint jointPoint , Object result){
        Object[] args = jointPoint.getArgs();

        Long workItemId = (Long)args[0];
        Long userId = (Long)args[1];

        String action = jointPoint.getSignature().getName().toUpperCase();

        ProcessInstanceWorkItem workItem = workItemRepository.findById(workItemId)
                .orElseThrow(() -> new RuntimeException("Work item not found"));

        ProcessInstance processInstance = workItem.getProcessInstance();

        ProcessInstanceHistory history = new ProcessInstanceHistory();
        history.setWorkItemId(workItemId);
        history.setUserId(userId);
        history.setAction(action);
        history.setActedAt(LocalDateTime.now());
        history.setProcessInstance(processInstance);

        historyRepository.save(history);
        log.info("[이력 저장] workItemId : {} , userId : {} , action : {} ", workItemId, userId, action);
    }
}
