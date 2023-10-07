package com.myblog1.service.impl;

import com.myblog1.entity.Post;
import com.myblog1.exception.ResourceNotFound;
import com.myblog1.payload.PostDto;
import com.myblog1.payload.PostResponse;
import com.myblog1.repository.PostRepository;
import com.myblog1.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto savePost(PostDto postDto) {
        Post post = maptoPost(postDto);
        Post savedPost = postRepository.save(post);
        PostDto dto = maptoPostDto(savedPost);
        return dto;
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post not found with id: " + id));
        postRepository.delete(post);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        // Check if the post exists in the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post not found with id: " + id));
        // Update the fields of the existing post
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        // Save the updated post
        Post updatedPost = postRepository.save(post);

        // Create and return the updated PostDto
        PostDto dto = maptoPostDto(updatedPost);

        return dto;
        }

    @Override
    public PostDto findPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post not found with id: " + id));

        PostDto dto = maptoPostDto(post);

        return dto;
    }

    @Override
    public PostResponse getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> pagePosts = postRepository.findAll(pageable);
        List<Post> posts = pagePosts.getContent();

        List<PostDto> postDtos = posts.stream().map(post -> maptoPostDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setPostDto(postDtos);
        postResponse.setPageNo(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElement(pagePosts.getTotalElements());
        postResponse.setLast(pagePosts.isLast());
        postResponse.setTotalPages(pagePosts.getTotalPages());

        return postResponse;
    }

    public PostDto maptoPostDto(Post post){
        PostDto dto = modelMapper.map(post, PostDto.class);
//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setContent(post.getContent());
//        dto.setDescription(post.getDescription());
        return dto;
    }

    public Post maptoPost(PostDto postDto){
        Post post = modelMapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());

        return post;
    }

}
