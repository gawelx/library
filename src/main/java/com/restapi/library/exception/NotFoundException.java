package com.restapi.library.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestException {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
