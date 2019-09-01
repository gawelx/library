package com.restapi.library.exception;

public class UnknownPersonStatusException extends RuntimeException {

    public UnknownPersonStatusException() {
        super("Unknown person status.");
    }

}
