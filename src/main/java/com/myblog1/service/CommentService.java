package com.myblog1.service;

import com.myblog1.entity.Post;
import com.myblog1.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    void deleteComment(long commentId);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest);

    List<CommentDto> getCommentById(long postId);
}
