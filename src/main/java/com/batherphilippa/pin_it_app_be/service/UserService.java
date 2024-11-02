package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.dto.UserLoginDTOIn;
import com.batherphilippa.pin_it_app_be.exceptions.UserExistsException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * UserService - the implementation of the User Service interface (IUserService).
 */
@Service
public class UserService implements IUserService{
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public UserService(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<UserDTOOut> findAll() {
        Set<User> users = userRepo.findAll();
        logger.info("UserService: all registered users returned");
        return convertUsersToDTOOutSet(users);
    }

    @Override
    public UserDTOOut save(UserDTOIn userDTOIn) {
        User user = new User();
        // map to User entity used in the DB
        modelMapper.map(userDTOIn, user);
        User newUser = userRepo.save(user);
        UserDTOOut userDTOOut = new UserDTOOut();
        // map to return the required output to controller
        modelMapper.map(newUser, userDTOOut);
        logger.info("UserService: user registration");
        return userDTOOut;
    }

    @Override
    public UserDTOOut findUserOnLogin(UserLoginDTOIn userLogin) {
        User user = userRepo.findByEmailAndPasswordNativeSQL(userLogin.getEmail(), userLogin.getPassword()).orElseThrow(() -> new UserNotFoundException(userLogin.getEmail()));
        logger.info("UserService: user login");
        return convertUserToDTOOut(user);
    }

    @Override
    public boolean findByEmail(String email) throws UserExistsException {
        if(userRepo.findByEmail(email).isPresent()) {
            throw new UserExistsException(email);
        }
        return false;
    }

    @Override
    public UserDTOOut findById(long userId) throws UserNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserDTOOut userDTOOut = new UserDTOOut();
        // map to return the required output to controller
        modelMapper.map(user, userDTOOut);
        logger.info("UserService: user found by ID");
        return userDTOOut;
    }

    @Override
    public UserDTOOut updateById(long userId, UserDTOIn userDTOIn) throws UserNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        modelMapper.map(userDTOIn, user);
        // set ID that was lost in mapping
        user.setId(userId);
        // save to repo
        User updatedUser = userRepo.save(user);
        // map to return the required output to controller
        UserDTOOut userDTOOut = new UserDTOOut();
        modelMapper.map(updatedUser, userDTOOut);
        logger.info("UserService: update user by ID");
        return userDTOOut;
    }

    @Override
    public void deleteById(long userId) throws UserNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepo.delete(user);
        logger.info("UserService: user identified by ID; entity deleted");
        userRepo.deleteById(userId);
    }

    private UserDTOOut convertUserToDTOOut(User user) {
        UserDTOOut userDTOOut = new UserDTOOut();
        logger.info("UserService: convertUserToDTOUT");
        modelMapper.map(user, userDTOOut);
        return userDTOOut;
    }

    private Set<UserDTOOut> convertUsersToDTOOutSet(Set<User> users) {
        Set<UserDTOOut> usersDTOOutSet = new HashSet<>();

        for (User user : users) {
            UserDTOOut userDTOOut = new UserDTOOut();
            modelMapper.map(user, userDTOOut);
            usersDTOOutSet.add(userDTOOut);
        }
        logger.info("UserService: convertUsersToDTOUTSet");
        return usersDTOOutSet;
    }
}
