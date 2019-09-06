package com.restapi.library.domain;

import com.restapi.library.exception.BadRequestException;

public enum BookStatus {
    AVAILABLE("available"),
    BORROWED("borrowed"),
    DAMAGED("damaged"),
    LOST("lost"),
    CANCELED("canceled");

    private String status;

    BookStatus(String status) {
        this.status = status;
    }

    public static BookStatus of(String status) {
        if (status == null) {
            throw new BadRequestException("Book status can't be null.");
        }
        switch (status) {
            case "available":
                return AVAILABLE;
            case "borrowed":
                return BORROWED;
            case "damaged":
                return DAMAGED;
            case "lost":
                return LOST;
            case "canceled":
                return CANCELED;
        }
        throw new BadRequestException("'" + status + "' is not valid book status.");
    }

    @Override
    public String toString() {
        return status;
    }

}
