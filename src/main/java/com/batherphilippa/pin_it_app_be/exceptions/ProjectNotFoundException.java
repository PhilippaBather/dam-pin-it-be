package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.EXCEPTION_PROJECT_NOT_FOUND_ID;

/**
 * ProjectNotFoundException - exception thrown when project not found by ID.
 */
public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(long userId) {
        super(EXCEPTION_PROJECT_NOT_FOUND_ID + userId);
    }
}
