package com.restapi.library.domain;

import com.restapi.library.exception.BadRequestException;

public enum PersonStatus {
    ACTIVE("active"),
    DELETED("deleted");

    private String restParamValue;

    PersonStatus(String restParamValue) {
        this.restParamValue = restParamValue;
    }

    public static PersonStatus of(String value) {
        if (value == null) {
            throw new BadRequestException("Person/borrower status can't be null.");
        }
        for (PersonStatus personStatus : values()) {
            if (personStatus.restParamValue.equals(value)) {
                return personStatus;
            }
        }
        throw new BadRequestException("'" + value + "' is not valid person/borrower status.");
    }

    @Override
    public String toString() {
        return restParamValue;
    }

}
