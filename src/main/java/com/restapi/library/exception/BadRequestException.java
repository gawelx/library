package com.restapi.library.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RestException {

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
