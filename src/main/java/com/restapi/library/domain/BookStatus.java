package com.restapi.library.domain;

import com.restapi.library.exception.BadRequestException;

public enum BookStatus {
    AVAILABLE("available"),
    BORROWED("borrowed"),
    DAMAGED("damaged"),
    LOST("lost"),
    CANCELED("canceled");

    private String restParamValue;

    BookStatus(String restParamValue) {
        this.restParamValue = restParamValue;
    }

    public static BookStatus of(String value) {
        if (value == null) {
            throw new BadRequestException("Book status can't be null.");
        }
        for (BookStatus bookStatus : values()) {
            if (bookStatus.restParamValue.equals(value)) {
                return bookStatus;
            }
        }
        throw new BadRequestException("'" + value + "' is not valid book status.");
    }

    @Override
    public String toString() {
        return restParamValue;
    }

}
