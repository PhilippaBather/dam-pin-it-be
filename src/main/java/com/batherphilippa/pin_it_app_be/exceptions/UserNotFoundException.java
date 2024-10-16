package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.EXCEPTION_USER_NOT_FOUND;

/**
 * UserNotFoundException - exception thrown when registered user not found by ID.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long userId) {
        super(EXCEPTION_USER_NOT_FOUND);
    }
}
