package com.batherphilippa.pin_it_app_be.exceptions.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * ValidationErrorModel - defines the validation error indicating the constraint, the detail, and its mapping.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorModel {

    private Timestamp timestamp;
    private String constraint;
    private String detail;
    private String mapping;
}
