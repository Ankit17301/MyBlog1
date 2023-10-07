package com.myblog1.controller;

import com.myblog1.payload.CommentDto;
import com.myblog1.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId, @RequestBody CommentDto commentDto) {
        CommentDto comment = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}/comments")
    public ResponseEntity<String> deleteComment(@PathVariable("id") long CommentId) {
        commentService.deleteComment(CommentId);
        return new ResponseEntity<>("Comment is deleted", HttpStatus.OK);
    }

    @PutMapping("/posts/{id}/comments/{id1}")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentRequest, @PathVariable("id") Long postId, @PathVariable("id1") Long commentId) {
        CommentDto dto = commentService.updateComment(postId, commentId, commentRequest);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments")
    public List<CommentDto> getCommentByPostId(@PathVariable(value = "postId") long postId, @RequestBody CommentDto commentDto) {
        return commentService.getCommentById(postId);
//        System.out.println(serviceAnkit);

    }


}