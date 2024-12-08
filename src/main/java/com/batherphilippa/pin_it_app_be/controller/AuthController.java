package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.dto.UserLoginDTOIn;
import com.batherphilippa.pin_it_app_be.exceptions.UnauthorisedException;
import com.batherphilippa.pin_it_app_be.exceptions.UserExistsException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.security.jwt.JwtResponse;
import com.batherphilippa.pin_it_app_be.security.jwt.JwtUtils;
import com.batherphilippa.pin_it_app_be.service.GuestService;
import com.batherphilippa.pin_it_app_be.service.UserAuthService;
import com.batherphilippa.pin_it_app_be.service.UserService;
import jakarta.mail.SendFailedException;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
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
    private GuestController guestController;
    @Autowired
    private GuestService guestService;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    @Value("${http.origins}")
    String LOCAL_HOST;

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

    @PostMapping("/users/pswd-reset/{email}")
    public ResponseEntity<com.batherphilippa.pin_it_app_be.model.User> resetPassword(@PathVariable String email) throws UserNotFoundException, SendFailedException {
        logger.info("start: AuthController_registerUser");
        // identify if user by this email exists, if not throw User not found error
        com.batherphilippa.pin_it_app_be.model.User user = userService.findByEmail(email);
        com.batherphilippa.pin_it_app_be.model.User updatdedUser = setRandomlyGeneratedPswd(user);
        userService.save(updatdedUser);
        return new ResponseEntity<>(updatdedUser, HttpStatus.OK);
    }
    private com.batherphilippa.pin_it_app_be.model.User setRandomlyGeneratedPswd(com.batherphilippa.pin_it_app_be.model.User user) throws SendFailedException {
        // if found, send an email with a temporary password
        String pswd = randomPasswordGenerator(8);
        encryptPswd(pswd, user);
        // update the password in the DB
        sendEmail(user.getEmail(), pswd);
        return user;
    }

    private String randomPasswordGenerator(int length) {
        return RandomStringUtils.random(length, 48, 76, true, true); // 2 digits, 2 uppercase, 2 special characters
    }

    private com.batherphilippa.pin_it_app_be.model.User encryptPswd(String pswd, com.batherphilippa.pin_it_app_be.model.User user) throws SendFailedException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(pswd));
        sendEmail(user.getEmail(), pswd);
        return user;
    }
    private void sendEmail(String recipient, String pswd) throws SendFailedException {
        // send user an email with reset instructions
        final String EMAIL_SUBJECT = "¡Pin-it App! Password Reset";
        final String EMAIL_BODY = String.format("You've requested a password reset. Please visit %s and login with the password %s.", LOCAL_HOST, pswd);
        guestController.sendEmail(recipient, EMAIL_SUBJECT, EMAIL_BODY);
    }

}
