package com.restapi.library.controller;

import com.restapi.library.domain.BookStatus;
import com.restapi.library.exception.BadRequestException;

public enum ProblemType {
    BOOK_LOST(BookStatus.LOST),
    BOOK_DAMAGED(BookStatus.DAMAGED),
    NONE(BookStatus.AVAILABLE);

    private BookStatus status;

    ProblemType(BookStatus status) {
        this.status = status;
    }

    public static ProblemType of(String problemType) {
        if (problemType == null) {
            throw new BadRequestException("The problem type can't be null.");
        }
        switch (problemType) {
            case "lost":
                return BOOK_LOST;
            case "damaged":
                return BOOK_DAMAGED;
            case "none":
                return NONE;
        }
        throw new BadRequestException("'" + problemType + "' is not valid borrowing problem type.");
    }

    public BookStatus getBookStatus() {
        return status;
    }
}
