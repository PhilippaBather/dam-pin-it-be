package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.EXCEPTION_USER_NOT_AUTHORISED;

/**
 * UserNotAuthorisedException - exception thrown when user is not authorised to carry out an action.
 */
public class UserNotAuthorisedException extends RuntimeException {

    public UserNotAuthorisedException(long userId) {
        super(EXCEPTION_USER_NOT_AUTHORISED + userId);
    }
}
