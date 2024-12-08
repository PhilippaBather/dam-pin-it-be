package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.*;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.service.ProjectService;
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
 * ProjectController - controller to manage CRUD operations for a project.
 */
@Validated
@RestController
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/projects/{userId}")
    public ResponseEntity<Set<ProjectUserDTOOut>> getAllUsersProjects(@PathVariable long userId) throws UserNotFoundException {
        Set<ProjectUserDTOOut> projectUsers = projectService.getAllUsersProjects(userId);
        logger.info("ProjectController: getAllUsersProjects");
        return new ResponseEntity<>(projectUsers, HttpStatus.OK);
    }

    @GetMapping("/project/user/{userId}/project/{projectId}")
    public ResponseEntity<ProjectAndPermissionsDTOOut> getProjectById(@PathVariable long projectId, @PathVariable long userId) throws ProjectNotFoundException {
        ProjectAndPermissionsDTOOut project = projectService.getProjectById(projectId, userId);
        logger.info("ProjectController: getProjectById");
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping("/projects/{userId}")
    public ResponseEntity<ProjectDTOOut> saveProject(@PathVariable long userId, @Valid @RequestBody ProjectDTOIn projectDTOIn) {
        User user = userService.findById(userId);
        logger.info("DTO in: " + projectDTOIn);
        ProjectDTOOut projectDTOOut = projectService.saveProject(projectDTOIn, user);
        logger.info("ProjectController: saveProject");
        return new ResponseEntity<>(projectDTOOut, HttpStatus.CREATED);
    }
/*
    // TODO
    @PostMapping("/projects/users/{userId}")
    public ResponseEntity<ProjectAndPermissionsDTOOut> addUserToExistingProject(@PathVariable String email, @Valid @RequestBody ProjectPermissionsDTOIn projectPermissionsDTOIn) throws UserNotFoundException {
        return null;
    }*/

    @PutMapping("/project/{projectId}")
    public ResponseEntity<ProjectAndPermissionsDTOOut> updateProjectById(@PathVariable long projectId, @Valid @RequestBody ProjectDTOIn projectDTOIn) throws ProjectNotFoundException {
        projectService.updateProjectById(projectId, String.valueOf(projectDTOIn.getDeadline()), projectDTOIn.getDescription(), projectDTOIn.getTitle());
        logger.info("ProjectController: updateProjectById");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable long projectId) throws ProjectNotFoundException {
        logger.info("ProjectController: deleteProjectById");
        projectService.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }
/*
    @GetMapping("/projects/owned-projects/{userId}")
    public ResponseEntity<List<OwnerProjectGuestsDTOOut>> getOwnedProjectsAndGuests(@PathVariable long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        List<OwnerProjectGuestsDTOOut> ownedGuestProjectsSet = projectService.getGuestsOnOwnerProjects(user.getEmail(), user.getId());
        return new ResponseEntity<>(ownedGuestProjectsSet, HttpStatus.OK);
    }*/

}
