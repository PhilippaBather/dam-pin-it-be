package com.batherphilippa.pin_it_app_be.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.batherphilippa.pin_it_app_be.exceptions.error.ErrorType.UNAUTHORISED_EXCEPTION;

/**
 * JwtAuthEntryPoint - JWT entry point to manage user credentials and authentication.
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String.valueOf(UNAUTHORISED_EXCEPTION));
    }
}
