package com.batherphilippa.pin_it_app_be.constants;

/**
 * Error Messages - constants defining the exception messages thrown.
 */
public class ErrorMessages {

    public static final String EXCEPTION_GUEST_ALREADY_EXISTS = "already exists as a guest on the project with ID ";
    public static final String EXCEPTION_GUEST_NOT_FOUND = "Guest not found on project with email ";
    public static final String EXCEPTION_PROJECT_NOT_FOUND_ID = "Project not found with ID ";
    public static final String EXCEPTION_TASK_NOT_FOUND_ID = "Task not found with ID ";
    public static final String EXCEPTION_USER_EXISTS = "User already exists with email ";
    public static final String EXCEPTION_USER_NOT_AUTHORISED = "User not authorised to carry out this action: only project Owner's may delete a project.";
    public static final String EXCEPTION_USER_NOT_FOUND_ID = "User not found with ID ";
    public static final String EXCEPTION_USER_NOT_FOUND_EMAIL = "User not found with email address ";
}
