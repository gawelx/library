package com.restapi.library.controller;

import com.restapi.library.exception.BadRequestException;

public enum BorrowingAction {

    BORROWING_PERIOD_UPDATE("periodUpdate"),
    BOOK_RETURN("bookReturn");

    private String restParamValue;

    BorrowingAction(String restParamValue) {
        this.restParamValue = restParamValue;
    }

    public static BorrowingAction of(String value) {
        if (value == null) {
            throw new BadRequestException("Action property can't be null.");
        }
        for (BorrowingAction borrowingAction : values()) {
            if (borrowingAction.restParamValue.equals(value)) {
                return borrowingAction;
            }
        }
        throw new BadRequestException("'" + value + "' is not valid borrowing action.");
    }

    @Override
    public String toString() {
        return restParamValue;
    }
}
