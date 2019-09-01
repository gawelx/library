package com.restapi.library.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("The book with given id doesn't exist.");
    }

}
