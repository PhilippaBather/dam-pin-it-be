package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.EXCEPTION_GUEST_NOT_FOUND;

public class GuestNotFoundException extends RuntimeException {

    public GuestNotFoundException(String email) {
        super(EXCEPTION_GUEST_NOT_FOUND + email);
    }
}
