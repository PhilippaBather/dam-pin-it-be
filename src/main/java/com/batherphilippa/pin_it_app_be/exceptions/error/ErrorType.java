package com.batherphilippa.pin_it_app_be.exceptions.error;

import java.sql.Timestamp;

/**
 * ErrorType - an enum to decribe the type of error: its code and state/name.
 */
public enum ErrorType {

    GUEST_PROJECT_EXISTS_EXCEPTION("409", "GUEST ALREADY EXISTS FOR PROJECT", new Timestamp(System.currentTimeMillis())),
    GUEST_NOT_FOUND_EXCEPTION("404", "GUEST NOT FOUND ON PROJECT", new Timestamp(System.currentTimeMillis())),
    PROJECT_NOT_FOUND_EXCEPTION("404", "PROJECT NOT FOUND", new Timestamp(System.currentTimeMillis())),
    TASK_NOT_FOUND_EXCEPTION("404", "TASK NOT FOUND", new Timestamp(System.currentTimeMillis())),
    UNAUTHORISED_EXCEPTION("404", "UNAUTHORISED EXCEPTION", new Timestamp(System.currentTimeMillis())),
    USER_UNAUTHORISED_EXCEPTION("404", "USER UNAUTHORISED EXCEPTION: INSUFFICIENT PERMISSIONS TO AUTHORISE ACTION", new Timestamp(System.currentTimeMillis())),
    USER_EXISTS_EXCEPTION("409", "USER EXISTS", new Timestamp(System.currentTimeMillis())),
    USER_NOT_FOUND_EXCEPTION("404", "USER NOT FOUND", new Timestamp(System.currentTimeMillis())),
    VALIDATION_UNPROCESSABLE_ENTITY_EXCEPTION("422", "VALIDATION ERROR: UNPROCESSEABLE ENTITY", new Timestamp(System.currentTimeMillis()));

    private final Timestamp timestamp;
    private final String code;
    private final String httpStatus;

    ErrorType(String code, String httpStatus, Timestamp timestamp) {
        this.timestamp = timestamp;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public String getCode() {
        return this.code;
    }

    public String getHttpStatus() {
        return this.httpStatus;
    }

}
