package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.dto.UserLoginDTOIn;
import com.batherphilippa.pin_it_app_be.exceptions.UnauthorisedException;
import com.batherphilippa.pin_it_app_be.exceptions.UserExistsException;
import com.batherphilippa.pin_it_app_be.security.jwt.JwtResponse;
import com.batherphilippa.pin_it_app_be.security.jwt.JwtUtils;
import com.batherphilippa.pin_it_app_be.service.UserAuthService;
import com.batherphilippa.pin_it_app_be.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AuthController - controller to manage CRUD operations for authentication.
 */
@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserAuthService authService;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/users/auth/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserLoginDTOIn user) throws UnauthorisedException {
        logger.info("start: AuthController_authenticateUser");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        logger.info("end: AuthController_authenticateUser");
        return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getUsername(), roles));
    }


    @PostMapping("/users/auth/signup")
    public ResponseEntity<UserDTOOut> registerUser(@Valid @RequestBody UserDTOIn newUser) throws UserExistsException {
        logger.info("start: AuthController_registerUser");
        authService.findDuplicateUserByEmail(newUser.getEmail());
        UserDTOOut userDTOOut = authService.save(newUser);
        logger.info("end: AuthController_registerUser");
        return new ResponseEntity<>(userDTOOut, HttpStatus.CREATED);
    }


}
