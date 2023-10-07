package com.myblog1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BlogAPIException extends Throwable {
    public BlogAPIException(HttpStatus badRequest, String invalidJwtSignature) { //400
        super(invalidJwtSignature);
    }
}
