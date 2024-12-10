package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.*;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotAuthorisedException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Permissions;
import com.batherphilippa.pin_it_app_be.model.ProjectUser;
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
    @PutMapping("/project/{projectId}/user/{userId}")
    public ResponseEntity<ProjectAndPermissionsDTOOut> updateProjectById(@PathVariable long projectId, @PathVariable long userId, @Valid @RequestBody ProjectDTOIn projectDTOIn) throws UserNotAuthorisedException, ProjectNotFoundException {
        ProjectUser projectUser = projectService.validatePermissions(projectId, userId);
        if(projectUser.getPermissions() == Permissions.VIEWER) {
            throw new UserNotAuthorisedException(userId);
        }
        projectService.updateProjectById(projectId, String.valueOf(projectDTOIn.getDeadline()), projectDTOIn.getDescription(), projectDTOIn.getTitle());
        logger.info("ProjectController: updateProjectById");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/project/{projectId}/user/{userId}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable long projectId, @PathVariable long userId) throws UserNotAuthorisedException, ProjectNotFoundException {
        logger.info("ProjectController: deleteProjectById");
        ProjectUser projectUser = projectService.validatePermissions(projectId, userId);
        if(projectUser.getPermissions() == Permissions.VIEWER || projectUser.getPermissions() == Permissions.EDITOR_RW) {
            throw new UserNotAuthorisedException(userId);
        }
        projectService.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }
}
