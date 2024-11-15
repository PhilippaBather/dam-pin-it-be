package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.*;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.*;
import com.batherphilippa.pin_it_app_be.repository.ProjectRepo;
import com.batherphilippa.pin_it_app_be.repository.ProjectUserRepo;
import com.batherphilippa.pin_it_app_be.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ProjectService - the implementation of the Project Service interface (IProjectService).
 */

@Service
public class ProjectService implements IProjectService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final ProjectRepo projectRepo;
    private final ProjectUserRepo projectUserRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public ProjectService(ProjectRepo projectRepo, ProjectUserRepo projectUserRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.projectRepo = projectRepo;
        this.projectUserRepo = projectUserRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public Set<ProjectUserDTOOut> getAllUsersProjects(long userId) throws UserNotFoundException {
        logger.info("ProjectService: getAllUsersProjects");

        userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Set<ProjectUser> projects = projectUserRepo.findAllUsersProjects(userId);

        if(projects.size() > 0) {
            return convertToProjectUserDTOOutSet(projects);
        }

        return new HashSet<>();
    }

    @Override
    public ProjectAndPermissionsDTOOut getProjectById(long projectId, long userId) throws ProjectNotFoundException {
        Project project = projectRepo.findByProjectId(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectUser projectUser = projectUserRepo.findProjectUserByProjectIdAndUserId(projectId, userId);
        ProjectAndPermissionsDTOOut projectOut = new ProjectAndPermissionsDTOOut();
        if(project != null) {
            modelMapper.map(project, projectOut);
            projectOut.setPermissions(projectUser.getPermissions());
        }
        logger.info("ProjectService: getProjectById: " + projectOut);
        return projectOut;
    }

    @Override
    public ProjectDTOOut saveProject(ProjectDTOIn projectDTOIn, User user) {
        logger.info("ProjectService: saveProject");

        // save project
        Project project = new Project();
        System.out.println(projectDTOIn.getDeadline());
        modelMapper.map(projectDTOIn, project);
        project.setProjectStatus(ProjectStatus.CURRENT);
        project = projectRepo.save(project);

        // map saved project to ProjectDTOOut
        ProjectDTOOut projectDTOOut = new ProjectDTOOut();
        modelMapper.map(project, projectDTOOut);

        // add project to user projectsSet
        user.getProjectsSet().add(project);
        userRepo.save(user);

        // update permissions in Project User join table
        ProjectUser projectUser = projectUserRepo.findProjectUserByProjectIdAndUserId(project.getId(), user.getId());
        projectUser.setPermissions(Permissions.OWNER);
        projectUserRepo.save(projectUser);

        return projectDTOOut;
    }

    @Override
    public void updateProjectById(long projectId, String deadline, String description, String title) throws ProjectNotFoundException {
        logger.info("UserService: updateProjectById");
        projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        projectRepo.updateProjectByProjectId(projectId, deadline, description, title);
    }
    @Override
    public void deleteProjectById(long projectId) throws ProjectNotFoundException {
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        projectUserRepo.deleteAllByProjectId(project.getId());
        projectRepo.deleteAllByProjectId(project.getId());
        logger.info("ProjectService: user identified by ID; entity deleted");
    }

    @Override
    public void deleteUserProjectsOnDeleteUser(User user) {
        Set<ProjectUser> usersOwnedProjects = projectUserRepo.findAllUsersProjects(user.getId());
        Set<Project> projects = usersOwnedProjects.stream().map(ProjectUser::getProject).collect(Collectors.toSet());
        projectUserRepo.deleteAllByUserId(user.getId());

        for(Project project : projects) {
            projectRepo.deleteAllUserOwnedProjects(project.getId());
        }
    }

    private Set<ProjectUserDTOOut> convertToProjectUserDTOOutSet(Set<ProjectUser> projects) {
        Set<ProjectUserDTOOut> projectUserDTOOutSet = new HashSet<>();
        for(ProjectUser projectUser : projects) {
            ProjectUserDTOOut projectUserDTOOut = new ProjectUserDTOOut();
            projectUserDTOOut.setProjectId(projectUser.getProject().getId());
            projectUserDTOOut.setUserId(projectUser.getUser().getId());
            projectUserDTOOut.setPermissions(projectUser.getPermissions());
            projectUserDTOOut.setTitle(projectUser.getProject().getTitle());
            projectUserDTOOutSet.add(projectUserDTOOut);
        }
        return projectUserDTOOutSet;
    }
}
