package com.batherphilippa.pin_it_app_be.exceptions.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ValidationErrorResponseModel - defines the validation error response model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponseModel {

    private String type;
    private List<ValidationErrorModel> errorsList;
}
