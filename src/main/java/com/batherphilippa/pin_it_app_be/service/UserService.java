package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.UserDTOIn;
import com.batherphilippa.pin_it_app_be.dto.UserDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Permissions;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.ProjectUser;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.repository.ProjectUserRepo;
import com.batherphilippa.pin_it_app_be.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * UserService - the implementation of the User Service interface (IUserService).
 */
@Service
public class UserService implements IUserService{
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ProjectUserRepo projectUserRepo;
    private final UserRepo userRepo;
    private final GuestService guestService;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public UserService(ProjectUserRepo projectUserRepo, UserRepo userRepo, GuestService guestService, ProjectService projectService, ModelMapper modelMapper) {
        this.projectUserRepo = projectUserRepo;
        this.userRepo = userRepo;
        this.guestService = guestService;
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<UserDTOOut> findAll() {
        Set<User> users = userRepo.findAll();
        logger.info("UserService: all registered users returned");
        return convertUsersToDTOOutSet(users);
    }
    @Override
    public User findById(long userId) throws UserNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        logger.info("UserService: user found by ID");
        return user;
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        logger.info("UserService: user found by email ");
        return user;
    }

    @Override
    public User save(User user) throws UserNotFoundException {
        return userRepo.save(user);
    }

    @Override
    public UserDTOOut updateById(long userId, UserDTOIn userDTOIn) throws UserNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        modelMapper.map(userDTOIn, user);
        // set ID lost in mapping
        user.setId(userId);
        // save to repo
        User updatedUser = userRepo.save(user);
        // map to return required output to controller
        UserDTOOut userDTOOut = new UserDTOOut();
        modelMapper.map(updatedUser, userDTOOut);
        logger.info("UserService: update user by ID");
        return userDTOOut;
    }

    @Override
    public void deleteById(long userId) throws UserNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        projectService.deleteUserProjectsOnDeleteUser(user);
        userRepo.delete(user.getId());
        logger.info("UserService: user identified by ID; entity deleted");
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

    public Map<Project, Permissions> updateGuestProjects(User user) {

        // get projects on which user is a guest and their permissions Map<Long,Permissions>
        Map<Project, Permissions> guestProjects = guestService.updateGuestDetails(user.getEmail());

        for (Map.Entry<Project, Permissions> entry : guestProjects.entrySet()) {
            user.getProjectsSet().add(entry.getKey());
        }
        User savedUser = userRepo.save(user);

        for (Map.Entry<Project, Permissions> entry : guestProjects.entrySet()) {
            ProjectUser projectUser = projectUserRepo.findProjectUserByProjectIdAndUserId(entry.getKey().getId(), user.getId());
            //projectUser.setPermissions(Permissions.EDITOR);
            projectUser.setPermissions(entry.getValue());
            projectUserRepo.save(projectUser);
        }

        // set notified as true in the guest

        guestService.updateNotifiedStatus(savedUser.getEmail());

        return guestProjects;
    }
}
