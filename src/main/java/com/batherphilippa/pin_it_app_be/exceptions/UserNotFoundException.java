package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.*;

/**
 * UserNotFoundException - exception thrown when registered user not found.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long userId) {
        super(EXCEPTION_USER_NOT_FOUND_ID + userId);
    }

    public UserNotFoundException(String email) {
        super(EXCEPTION_USER_NOT_FOUND_EMAIL + email);
    }
}
