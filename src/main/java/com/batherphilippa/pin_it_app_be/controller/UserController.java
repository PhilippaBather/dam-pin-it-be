package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * UserController - controller to manage CRUD operations for a registering or registered users.
 */
@Validated
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Set<UserDTOOut>> getAllUsers() {
        Set<UserDTOOut> users = userService.findAll();
        logger.info("UserController: getAllUsers");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/users/registration")
    public ResponseEntity<UserDTOOut> registerUser(@Valid @RequestBody UserDTOIn userDTOIn) {
        UserDTOOut userDTOOut = userService.save(userDTOIn);
        logger.info("UserController: registerUser");
        return new ResponseEntity<>(userDTOOut, HttpStatus.CREATED);
    }

    @GetMapping("/users/login/{userId}")
    public ResponseEntity<UserDTOOut> getUserById(@PathVariable long userId) throws UserNotFoundException {
        UserDTOOut userDTOOut = userService.findById(userId);
        logger.info("UserController: getUserById");
        return new ResponseEntity<>(userDTOOut, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTOOut> updateUserById(@PathVariable long userId, @Valid @RequestBody UserDTOIn userDTOIn) throws UserNotFoundException {
        UserDTOOut userDTOOut = userService.updateById(userId, userDTOIn);
        logger.info("UserController: updateUserById");
        return new ResponseEntity<>(userDTOOut, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long userId) throws UserNotFoundException{
        userService.deleteById(userId);
        logger.info("UserController: deleteUserById");
        return ResponseEntity.noContent().build();
    }
}
