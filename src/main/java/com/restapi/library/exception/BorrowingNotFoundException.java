package com.restapi.library.exception;

public class BorrowingNotFoundException extends RuntimeException {

    public BorrowingNotFoundException() {
        super("The borrowing with given id doesn't exist.");
    }

}
