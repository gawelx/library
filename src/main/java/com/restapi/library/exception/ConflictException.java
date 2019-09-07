package com.restapi.library.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends RestException {

    public ConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }

}
