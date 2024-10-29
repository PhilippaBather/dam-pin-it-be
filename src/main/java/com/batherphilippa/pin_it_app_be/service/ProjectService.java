package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.*;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.Status;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.repository.ProjectRepo;
import com.batherphilippa.pin_it_app_be.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

/**
 * ProjectService - the implementation of the Project Service interface (IProjectService).
 */

@Service
public class ProjectService implements IProjectService{

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public ProjectService(ProjectRepo projectRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public Set<ProjectUserDTOOut> getAllUsersProjects(long userId) {
        userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        logger.info("ProjectService: getAllUsersProjects");
        return projectRepo.findAllUsersProjects(userId);
    }

    @Override
    public ProjectAndPermissionsDTOOut getProjectById(long projectId) throws ProjectNotFoundException{
        Project project = projectRepo.findProjectById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectAndPermissionsDTOOut projectOut = new ProjectAndPermissionsDTOOut();
        modelMapper.map(project, projectOut);
        logger.info("ProjectService: getProjectById");
        return projectOut;
    }

    @Override
    public ProjectDTOOut saveProject(ProjectDTOIn projectDTOIn) {
        Project project = new Project();
        modelMapper.map(projectDTOIn, project);
        project = projectRepo.save(project);
        ProjectDTOOut projectDTOOut = new ProjectDTOOut();
        modelMapper.map(projectDTOIn, project);
        logger.info("ProjectService: saveProject");
        return projectDTOOut;
    }

    @Override
    public ProjectDTOOut updateProjectById(long projectId, ProjectDTOIn projectDTOIn) {
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        modelMapper.map(projectDTOIn, project);
        // set ID lost in mapping
        project.setId(projectId);
        // set project creation date
        project.setCreationDate(LocalDate.now());
        // project status CURRENT by default if null
        if(project.getProjectStatus() == null) {
            project.setProjectStatus(Status.CURRENT);
        }
        // save to repo
        Project updatedProject = projectRepo.save(project);
        // map to return required output to controller
        ProjectDTOOut projectDTOOut = new ProjectDTOOut();
        modelMapper.map(updatedProject, projectDTOOut);
        logger.info("UserService: updateProjectById");
        return projectDTOOut;
    }

    @Override
    public void deleteProjectById(long projectId) throws ProjectNotFoundException {
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        projectRepo.delete(project);
        logger.info("ProjectService: user identified by ID; entity deleted");
    }

    private UserDTOOut convertUserToDTOOut(User user) {
        UserDTOOut userDTOOut = new UserDTOOut();
        logger.info("UserService: convertUserToDTOUT");
        modelMapper.map(user, userDTOOut);
        return userDTOOut;
    }
}
