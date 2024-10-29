package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.ProjectAndPermissionsDTOOut;
import com.batherphilippa.pin_it_app_be.dto.ProjectDTOIn;
import com.batherphilippa.pin_it_app_be.dto.ProjectDTOOut;
import com.batherphilippa.pin_it_app_be.dto.ProjectUserDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.service.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Validated
@RestController
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public ResponseEntity<Set<ProjectUserDTOOut>> getAllUsersProjects(@PathVariable long userId) throws UserNotFoundException {
        Set<ProjectUserDTOOut> projects =  projectService.getAllUsersProjects(userId);
        logger.info("ProjectController: getAllUsersProjects");
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectAndPermissionsDTOOut> getProjectById(@PathVariable long projectId) throws ProjectNotFoundException {
        ProjectAndPermissionsDTOOut project = projectService.getProjectById(projectId);
        logger.info("ProjectController: getProjectById");
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectDTOOut> saveProject(@Valid @RequestBody ProjectDTOIn projectDTOIn) {
        ProjectDTOOut projectDTOOut = projectService.saveProject(projectDTOIn);
        logger.info("ProjectController: saveProject");
        return new ResponseEntity<>(projectDTOOut, HttpStatus.CREATED);
    }

    @PutMapping("/project/{projectId}")
    public ResponseEntity<ProjectDTOOut> updateProjectById(@PathVariable long projectId, @Valid @RequestBody ProjectDTOIn projectDTOIn) throws ProjectNotFoundException {
        ProjectDTOOut projectDTOOut = projectService.updateProjectById(projectId, projectDTOIn);
        logger.info("ProjectController: updateProjectById");
        return new ResponseEntity<>(projectDTOOut, HttpStatus.OK);
    }

    @DeleteMapping("/project/{projectId")
    public ResponseEntity<Void> deleteProjectById(@PathVariable long projectId) throws ProjectNotFoundException {
        logger.info("ProjectController: deleteProjectById");
        projectService.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }

}
