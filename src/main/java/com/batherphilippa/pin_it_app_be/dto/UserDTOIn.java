package com.batherphilippa.pin_it_app_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDTOIn - defines the data transfer object for an incoming user object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOIn {

    private String forename;
    private String surname;
    private String email;
    private String password;

}
