package com.restapi.library.exception;

public class BorrowerNotFoundException extends RuntimeException {

    public BorrowerNotFoundException() {
        super("The borrower with given id doesn't exist.");
    }

}
