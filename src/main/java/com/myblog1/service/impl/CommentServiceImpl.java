package com.myblog1.service.impl;

import com.myblog1.entity.Comment;
import com.myblog1.entity.Post;
import com.myblog1.payload.CommentDto;
import com.myblog1.repository.CommentRepository;
import com.myblog1.repository.PostRepository;
import com.myblog1.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.myblog1.exception.ResourceNotFound;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Page not found with id: " + postId));
        // set post to comment entity
        comment.setPost(post);
        // comment entity to DB
        Comment savedComment = commentRepository.save(comment);
        CommentDto dto = mapToDTO(savedComment);
        return dto;
    }
    @Override
    public void deleteComment(long commentId) {
        // Check if the comment exists
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFound("Comment not found with id: " + commentId));

        // Finally, delete the comment from the database
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Page not found with id: " + postId));
        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFound("Comment not found with id: " + commentId));

        comment.setPost(post);
        comment.setId(commentId);
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment save = commentRepository.save(comment);
        CommentDto commentDto = mapToDTO(save);
        return commentDto;

    }

    @Override
    public List<CommentDto> getCommentById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Page not found with id: " + postId));

        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> dto = comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());

        return dto;
    }

    private CommentDto mapToDTO(Comment savedComment) {
        CommentDto map = mapper.map(savedComment, CommentDto.class);
        return map;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
        return comment;
    }
    }


