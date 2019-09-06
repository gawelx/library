package com.restapi.library.exception;

import org.springframework.http.HttpStatus;

public abstract class RestException extends RuntimeException {

    public RestException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();

}
