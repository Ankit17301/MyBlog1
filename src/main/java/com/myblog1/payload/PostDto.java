package com.myblog1.payload;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PostDto {
    private Long id;

    // title should not be null or empty
    // title should have at least 2 characters
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    // title should not be null or empty
    // title should have at least 2 characters
    @NotEmpty
    @Size(min = 3, message = "Post description should have at least 3 characters")
    private String description;

    // title should not be null or empty
    // title should have at least 2 characters
    @NotEmpty
    @Size(min = 4, message = "Post content should have at least 4 characters")
    private String content;

}
