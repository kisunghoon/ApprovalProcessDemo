package com.salesforce.approvalprocessdemo.aop;

import com.salesforce.approvalprocessdemo.domain.Post;
import com.salesforce.approvalprocessdemo.repository.PostRepository;
import com.salesforce.approvalprocessdemo.type.ApproveStatus;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PostEditLockAspect {

    private final PostRepository postRepository;

    @Before("@annotation(CheckPostEditable)")
    public void validatePostEditable(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if(args.length == 0 || !(args[0] instanceof Long)) {
            throw new RuntimeException("게시글 ID가 필요합니다.");
        }

        Long postId = (Long) args[0];

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));


        if(post.getStatus() == ApproveStatus.APPROVED){
            throw new RuntimeException("승인 완료된 게시글은 수정하거나 삭제할 수 없습니다.");
        }
    }
}
