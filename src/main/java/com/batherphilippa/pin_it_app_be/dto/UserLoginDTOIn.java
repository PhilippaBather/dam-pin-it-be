package com.batherphilippa.pin_it_app_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserLoginDTOIn - defines the data transfer object for an incoming login request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTOIn {

    private String email;
    private String password;
}
