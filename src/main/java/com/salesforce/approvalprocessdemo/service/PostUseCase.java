package com.salesforce.approvalprocessdemo.service;

import com.salesforce.approvalprocessdemo.domain.Post;
import com.salesforce.approvalprocessdemo.dto.CreatePostRequest;
import com.salesforce.approvalprocessdemo.dto.PostResponse;
import com.salesforce.approvalprocessdemo.dto.UpdatePostRequest;

public interface PostUseCase {

    void createPost(CreatePostRequest request);

    void requestApproval(Long postId , Long templateId, String createdBy);

    PostResponse getPost(Long postId);

    void updatePost(Long postId , UpdatePostRequest request);

    void deletePost(Long postId);
}
