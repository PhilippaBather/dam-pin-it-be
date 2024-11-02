package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.exceptions.error.ErrorType.UNAUTHORISED_EXCEPTION;

/**
 * UnauthorisedException - exception thrown when user makes a forbidden request.
 */
public class UnauthorisedException extends RuntimeException {

    public UnauthorisedException() {
        super(String.valueOf(UNAUTHORISED_EXCEPTION));
    }
}
