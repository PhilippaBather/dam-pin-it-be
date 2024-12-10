package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.User;

import java.util.Set;

/**
 * IUserService - User Service interface defining the methods to be used.
 */
public interface IUserService {

    Set<UserDTOOut> findAll();
    User findById(long userId) throws UserNotFoundException;
    User findByEmail(String email) throws UserNotFoundException;
    User save(User user) throws UserNotFoundException;
    UserDTOOut updateById(long userId, UserDTOIn userDTOIn) throws UserNotFoundException;
    void deleteById(long userId) throws UserNotFoundException;
}
