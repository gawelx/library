package com.restapi.library.domain;

public enum BookStatus {
    AVAILABLE("available"),
    BORROWED("borrowed"),
    DAMAGED("damaged"),
    LOST("lost");

    private String status;

    private BookStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

    static BookStatus getInstance(String status) {
        switch (status) {
            case "available":
                return AVAILABLE;
            case "borrowed":
                return BORROWED;
            case "damaged":
                return DAMAGED;
            case "lost":
                return LOST;
        }
        throw new IllegalStateException("Unknown book status.");
    }
}
