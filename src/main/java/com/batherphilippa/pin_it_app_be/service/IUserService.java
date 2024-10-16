package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;

import java.util.Set;

public interface IUserService {

    Set<UserDTOOut> findAll();
    UserDTOOut save(UserDTOIn userDTOIn);
    UserDTOOut findById(long userId);
    UserDTOOut updateById(long userId, UserDTOIn userDTOIn);
    void deleteById(long userId);
}
