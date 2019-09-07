package com.restapi.library.domain;

import com.restapi.library.exception.BadRequestException;

public enum PenaltyCause {
    BOOK_LOST("bookLost"),
    BOOK_DAMAGED("bookDamaged"),
    DEADLINE_EXCEEDED("deadlineExceeded");

    private String restParamValue;

    PenaltyCause(String restParamValue) {
        this.restParamValue = restParamValue;
    }

    public static PenaltyCause of(String value) {
        if (value == null) {
            throw new BadRequestException("The penalty cause can't be null.");
        }
        for (PenaltyCause penaltyCause : values()) {
            if (penaltyCause.restParamValue.equals(value)) {
                return penaltyCause;
            }
        }
        throw new BadRequestException("'" + value + "' is not valid penalty cause.");
    }

    @Override
    public String toString() {
        return restParamValue;
    }
}
