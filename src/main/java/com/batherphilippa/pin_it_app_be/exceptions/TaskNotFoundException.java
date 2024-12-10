package com.batherphilippa.pin_it_app_be.exceptions;

import static com.batherphilippa.pin_it_app_be.constants.ErrorMessages.EXCEPTION_TASK_NOT_FOUND_ID;

/**
 * TaskNotFoundException - exception thrown when task not found by ID.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(long taskId) {
        super(EXCEPTION_TASK_NOT_FOUND_ID + taskId);
    }
}
