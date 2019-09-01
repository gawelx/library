package com.restapi.library.exception;

public class UnknownBookStatusException extends RuntimeException {

    public UnknownBookStatusException() {
        super("Unknown book status.");
    }

}
