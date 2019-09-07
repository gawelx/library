package com.restapi.library.controller;

import com.restapi.library.domain.BookStatus;
import com.restapi.library.exception.BadRequestException;

public enum ProblemType {
    BOOK_LOST(BookStatus.LOST, "lost"),
    BOOK_DAMAGED(BookStatus.DAMAGED, "damaged"),
    NONE(BookStatus.AVAILABLE, "none");

    private BookStatus status;
    private String restParamValue;

    ProblemType(BookStatus status, String restParamValue) {
        this.status = status;
        this.restParamValue = restParamValue;
    }

    public static ProblemType of(String value) {
        if (value == null) {
            throw new BadRequestException("The problem type can't be null.");
        }
        for (ProblemType problemType : values()) {
            if (problemType.restParamValue.equals(value)) {
                return problemType;
            }
        }
        throw new BadRequestException("'" + value + "' is not valid borrowing problem type.");
    }

    public BookStatus getBookStatus() {
        return status;
    }
}
