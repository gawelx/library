package com.restapi.library.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            PersonNotFoundException.class,
            BorrowerNotFoundException.class,
            BookTitleNotFoundException.class,
            BookNotFoundException.class,
            BorrowingNotFoundException.class
    })
    protected ResponseEntity<Object> handleEntityNotFoundException(RuntimeException exception, WebRequest request) {
        return buildResponseEntity(exception, request, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> buildResponseEntity(RuntimeException exception, WebRequest request, HttpStatus status) {
        Map<String, Object> bodyOfResponse = new LinkedHashMap<>();
        bodyOfResponse.put("timestamp", LocalDateTime.now());
        bodyOfResponse.put("status", status.value());
        bodyOfResponse.put("error", status.getReasonPhrase());
        bodyOfResponse.put("message", exception.getMessage());
        bodyOfResponse.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return handleExceptionInternal(exception, bodyOfResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({
            UnknownBookStatusException.class,
            UnknownPersonStatusException.class
    })
    protected ResponseEntity<Object> handleUnknownBookStatus(RuntimeException exception, WebRequest request) {
        return buildResponseEntity(exception, request, HttpStatus.BAD_REQUEST);
    }

}