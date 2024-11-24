package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.dto.UserLoginDTOIn;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.service.GuestService;
import com.batherphilippa.pin_it_app_be.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * UserController - controller to manage CRUD operations for registering or registered users.
 */
@Validated
@RestController
@CrossOrigin()
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ModelMapper modelMapper;

    private final GuestService guestService;
    private final UserService userService;
    public UserController(GuestService guestService, UserService userService, ModelMapper modelMapper) {
        this.guestService = guestService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @GetMapping("/users")
    public ResponseEntity<Set<UserDTOOut>> getAllUsers() {
        Set<UserDTOOut> users = userService.findAll();
        logger.info("UserController: getAllUsers");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTOOut> getUserById(@PathVariable long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        UserDTOOut userDTOOut = new UserDTOOut();
        // map to return the required output to controller
        modelMapper.map(user, userDTOOut);
        logger.info("UserController: getUserById");
        return new ResponseEntity<>(userDTOOut, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTOOut> getUserDetails(@Valid @RequestBody UserLoginDTOIn userLoginDTOIn) throws UserNotFoundException {
        logger.info("DTO in: " + userLoginDTOIn);
        User user = userService.findByEmail(userLoginDTOIn.getEmail());
        logger.info("UserController: updateGuestProjectNotificationStatus");
        // if true, set field to control FE alert management
        boolean isNotification = updateGuestDetails(user);
        UserDTOOut userDTOOut = new UserDTOOut();
        modelMapper.map(user, userDTOOut);
        userDTOOut.setUserNotified(isNotification);
        return new ResponseEntity<>(userDTOOut, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTOOut> updateUserById(@PathVariable long userId, @Valid @RequestBody UserDTOIn userDTOIn) throws UserNotFoundException {
        UserDTOOut userDTOOut = userService.updateById(userId, userDTOIn);
        logger.info("UserController: updateUserById");
        return new ResponseEntity<>(userDTOOut, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long userId) throws UserNotFoundException {
        userService.deleteById(userId);
        logger.info("UserController: deleteUserById");
        return ResponseEntity.noContent().build();
    }

    private boolean updateGuestDetails(User user) {
        logger.info("UserController: updateGuestDetails");
        // if user email in Guests table and notification = false, !notification
        return guestService.updateGuestDetails(user);
    }
}
