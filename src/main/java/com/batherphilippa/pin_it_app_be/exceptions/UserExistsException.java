package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.EXCEPTION_USER_EXISTS;

public class UserExistsException extends RuntimeException {

    public UserExistsException(String email) {
        super(EXCEPTION_USER_EXISTS + email);
    }
}
