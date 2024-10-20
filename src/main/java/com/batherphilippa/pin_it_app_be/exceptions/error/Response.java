package com.batherphilippa.pin_it_app_be.exceptions.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * Response - defines the error response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private Timestamp timestamp;
    private String code;
    private String type;
    private String message;
}
