package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.dto.UserLoginDTOIn;

import java.util.Set;

/**
 * IUserService - User Service interface defining the methods to be used.
 */
public interface IUserService {

    Set<UserDTOOut> findAll();
    UserDTOOut save(UserDTOIn userDTOIn);
    boolean findByEmail(String email);
    UserDTOOut findById(long userId);
    UserDTOOut findUserOnLogin(UserLoginDTOIn user);
    UserDTOOut updateById(long userId, UserDTOIn userDTOIn);
    void deleteById(long userId);
}
