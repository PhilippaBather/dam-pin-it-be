package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;

import java.util.Set;

/**
 * IUserService - User Service interface defining the methods to be used.
 */
public interface IUserService {

    Set<UserDTOOut> findAll();
    UserDTOOut findById(long userId);
    UserDTOOut updateById(long userId, UserDTOIn userDTOIn);
    void deleteById(long userId);
}
