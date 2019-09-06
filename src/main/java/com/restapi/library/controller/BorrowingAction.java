package com.restapi.library.controller;

import com.restapi.library.exception.BadRequestException;

public enum BorrowingAction {

    BORROWING_PERIOD_UPDATE("periodUpdate"),
    BOOK_RETURN("bookReturn");

    private String action;

    BorrowingAction(String action) {
        this.action = action;
    }

    public static BorrowingAction of(String action) {
        if (action == null) {
            throw new BadRequestException("Action property can't be null.");
        }
        switch (action) {
            case "periodUpdate":
                return BORROWING_PERIOD_UPDATE;
            case "bookReturn":
                return BOOK_RETURN;
        }
        throw new BadRequestException("'" + action + "' is not valid borrowing action.");
    }

    @Override
    public String toString() {
        return action;
    }
}
