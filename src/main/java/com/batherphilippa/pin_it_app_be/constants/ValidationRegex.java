package com.batherphilippa.pin_it_app_be.constants;

/**
 * ValidationRegex - constants defining Regex for validation.
 */
public class ValidationRegex {

    public static final String VALIDATION_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!=$_¿.* ).{8,25}$";
}
