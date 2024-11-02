package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.UserExistsException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthService.class);

    private final ModelMapper modelMapper;

    private final UserRepo userRepo;

    public UserAuthService(ModelMapper modelMapper, UserRepo userRepo) {
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("start: ProprietorUserDetailsService_loadUserByUsername");
        User user = findUserByEmail(email);
        logger.info("end: ProprietorUserDetailsService_loadUserByUsername");
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }

    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public void findDuplicateUserByEmail(String email) throws UserExistsException {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isPresent()) {
            throw new UserExistsException(email);
        }
    }



    public UserDTOOut save(UserDTOIn userDTOIn) {
        User user = new User();

        // map to User entity used in the DB
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = userDTOIn.getPassword();
        modelMapper.map(userDTOIn, user);
        user.setPassword(passwordEncoder.encode(password));

        User newUser = userRepo.save(user);
        UserDTOOut userDTOOut = new UserDTOOut();

        // map to return the required output to controller
        modelMapper.map(newUser, userDTOOut);
        logger.info("UserService: user registration");
        return userDTOOut;
    }


}
