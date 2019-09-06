package com.restapi.library.domain;

import com.restapi.library.exception.BadRequestException;

public enum PersonStatus {
    ACTIVE("active"),
    DELETED("deleted");

    private String status;

    PersonStatus(String status) {
        this.status = status;
    }

    public static PersonStatus of(String status) {
        if (status == null) {
            throw new BadRequestException("Person/borrower status can't be null.");
        }
        switch (status) {
            case "active":
                return ACTIVE;
            case "deleted":
                return DELETED;
        }
        throw new BadRequestException("'" + status + "' is not valid person/borrower status.");
    }

    @Override
    public String toString() {
        return status;
    }

}
