package com.restapi.library.exception;

import com.restapi.library.dto.BodyOfResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestException.class)
    protected ResponseEntity<Object> handleRestException(RestException exception, WebRequest request) {
        return buildResponseEntity(exception, request, exception.getHttpStatus());
    }

    private ResponseEntity<Object> buildResponseEntity(RuntimeException exception, WebRequest request,
                                                       HttpStatus status) {
        BodyOfResponseDto bodyOfResponseDto = new BodyOfResponseDto(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return handleExceptionInternal(exception, bodyOfResponseDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException exception, WebRequest request) {
        return buildResponseEntity(exception, request, HttpStatus.BAD_REQUEST);
    }

}
