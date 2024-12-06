package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.EXCEPTION_GUEST_ALREADY_EXISTS;

public class GuestInvitationException extends RuntimeException {

    public GuestInvitationException(String email, long projectId) {
        super(String.format("%s %s %d", email, EXCEPTION_GUEST_ALREADY_EXISTS, projectId));
    }

}
