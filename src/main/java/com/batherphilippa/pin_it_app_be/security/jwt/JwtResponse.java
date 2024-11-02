package com.batherphilippa.pin_it_app_be.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JwtResponse - defines the response object for the Json Web Token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String jsonToken;
    private String userEmail;
    private List<String> roles;

}
