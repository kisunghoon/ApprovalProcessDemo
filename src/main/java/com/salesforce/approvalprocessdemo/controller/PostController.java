package com.salesforce.approvalprocessdemo.controller;


import com.salesforce.approvalprocessdemo.dto.CreatePostRequest;
import com.salesforce.approvalprocessdemo.dto.PostResponse;
import com.salesforce.approvalprocessdemo.dto.RequestApproval;
import com.salesforce.approvalprocessdemo.dto.UpdatePostRequest;
import com.salesforce.approvalprocessdemo.service.PostUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostUseCase postUseCase;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest request) {

        postUseCase.createPost(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/request-approval")
    public ResponseEntity<Void> requestApproval(@PathVariable Long postId ,
                                                @RequestBody RequestApproval request){
        postUseCase.requestApproval(postId,request.getTemplateId(),request.getCreatedBy());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse post = postUseCase.getPost(postId);

        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId , @RequestBody UpdatePostRequest request)
    {
        postUseCase.updatePost(postId,request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId){

        postUseCase.deletePost(postId);
        return ResponseEntity.ok().build();
    }





}
