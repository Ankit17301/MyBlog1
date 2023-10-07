package com.myblog1.controller;

import com.myblog1.payload.PostDto;
import com.myblog1.payload.PostResponse;
import com.myblog1.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/post")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) { //Constructor based Dependency Injection
        this.postService = postService;
    }

    //http://localhost:8080/api/post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> savePost(@Valid @RequestBody PostDto postDto, BindingResult result) {
        // We use question mark here for wildcard generic because if anyone have return type ResponseEntity and have different generics then we prefer to use this instead of writing particular generics name
        if(result.hasErrors())
        {
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto = postService.savePost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);  //201
    }

    //http://localhost:8080/api/post/1
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("Post is deleted", HttpStatus.OK);
    }

    //http://localhost:8080/api/post/2
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable("id") long id, @RequestBody PostDto postDto) {
        PostDto updatedPostDto = postService.updatePost(id, postDto);
        return new ResponseEntity<>(updatedPostDto, HttpStatus.OK);
    }

    //http://localhost:8080/api/post/2
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> findPostById(@PathVariable("id") long id) {
        PostDto dto = postService.findPostById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //    http://localhost:8080/api/post?pageNo=0&pageSize=3&sortBy=title&sortDir=desc
    @GetMapping
    public PostResponse getPosts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = "7", required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        //It means @RequestParam is used to extract resources from thr URL like having parameter like URL have "pageNo" which is actually a parameter and also giving default value if someone give no value then it takes value by default and requires means it is mandatory to push value into the url or not

        PostResponse postResponse = postService.getPosts(pageNo, pageSize, sortBy, sortDir);
        return postResponse;
    }

}
