package com.salesforce.approvalprocessdemo.service.impl;

import com.salesforce.approvalprocessdemo.aop.CheckPostEditable;
import com.salesforce.approvalprocessdemo.domain.Post;
import com.salesforce.approvalprocessdemo.domain.ProcessInstance;
import com.salesforce.approvalprocessdemo.dto.CreatePostRequest;
import com.salesforce.approvalprocessdemo.dto.PostResponse;
import com.salesforce.approvalprocessdemo.dto.UpdatePostRequest;
import com.salesforce.approvalprocessdemo.repository.PostRepository;
import com.salesforce.approvalprocessdemo.repository.ProcessInstanceRepository;
import com.salesforce.approvalprocessdemo.service.PostUseCase;
import com.salesforce.approvalprocessdemo.service.ProcessUseCase;
import com.salesforce.approvalprocessdemo.type.ApproveStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService implements PostUseCase {

    private final PostRepository postRepository;
    private final ProcessUseCase processUseCase;
    private final ProcessInstanceRepository processInstanceRepository;

    @Override
    public void createPost(CreatePostRequest request) {

        //게시글 기본 정보 생성

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setStatus(ApproveStatus.NONE);

        Post savePost = postRepository.save(post);

        postRepository.save(savePost);
    }

    @Transactional
    public void requestApproval(Long postId , Long templateId, String createdBy){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));

        ProcessInstance process = processUseCase.createProcessFromTemplate(
                templateId,
                "POST",
                postId,
                createdBy
        );

        processInstanceRepository.save(process);

        post.setProcessInstance(process);
        post.setStatus(ApproveStatus.REQUEST);

        postRepository.save(post);

    }

    @Override
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        return PostResponse.builder()
                .postId(post.getPostId())
                .titile(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus().name())
                .processInstanceId(
                        post.getProcessInstance() != null ? post.getProcessInstance().getProcessInstanceId() : null
                )
                .build();
    }

    @Override
    @CheckPostEditable
    public void updatePost(Long postId, UpdatePostRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        post.setTitle(request.getTitie());
        post.setContent(request.getContent());
    }

    @Override
    @CheckPostEditable
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));


        postRepository.delete(post);
    }
}
