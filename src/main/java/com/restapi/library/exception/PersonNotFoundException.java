package com.restapi.library.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException() {
        super("The person with given id doesn't exist.");
    }

}
