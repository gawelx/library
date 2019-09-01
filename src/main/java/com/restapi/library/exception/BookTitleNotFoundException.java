package com.restapi.library.exception;

public class BookTitleNotFoundException extends RuntimeException {

    public BookTitleNotFoundException() {
        super("The book title with given id doesn't exist.");
    }

}
