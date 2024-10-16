package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.EXCEPTION_USER_NOT_FOUND;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long userId) {
        super(EXCEPTION_USER_NOT_FOUND);
    }
}
