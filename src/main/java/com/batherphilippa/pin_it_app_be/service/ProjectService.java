package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.*;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.*;
import com.batherphilippa.pin_it_app_be.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ProjectService - the implementation of the Project Service interface (IProjectService).
 */

@Service
public class ProjectService implements IProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final ProjectRepo projectRepo;
    private final ProjectUserRepo projectUserRepo;
    private final GuestRepo guestRepo;
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public ProjectService(GuestRepo guestRepo, ProjectRepo projectRepo, ProjectUserRepo projectUserRepo, TaskRepo taskRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.guestRepo = guestRepo;
        this.projectRepo = projectRepo;
        this.projectUserRepo = projectUserRepo;
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public Set<ProjectUserDTOOut> getAllUsersProjects(long userId) throws UserNotFoundException {
        LOGGER.info("ProjectService: getAllUsersProjects");

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
        LOGGER.info("ProjectService: getProjectById: " + projectOut);
        return projectOut;
    }

    @Override
    public Project getProjectByIdForGuest(long projectId, long userId) throws ProjectNotFoundException {
        return projectRepo.findByProjectId(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    /// NEW
    @Override
    public Project getProjectGuestById(long projectId) throws ProjectNotFoundException {
        return projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Override
    public ProjectDTOOut saveProject(ProjectDTOIn projectDTOIn, User user) {
        LOGGER.info("ProjectService: saveProject");

        // save project
        Project project = new Project();
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

        // add projectUser to userProjectsPermissions set
        user.getUserProjectsPermissions().add(projectUser);

        return projectDTOOut;
    }

    // save update project
    @Override
    public Project save(Project project) throws ProjectNotFoundException {
        return projectRepo.save(project);
    }

    @Override
    public void updateProjectById(long projectId, String deadline, String description, String title) throws ProjectNotFoundException {
        LOGGER.info("UserService: updateProjectById");
        projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        projectRepo.updateProjectByProjectId(projectId, deadline, description, title);
    }
    @Override
    public void deleteProjectById(long projectId) throws ProjectNotFoundException {
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        // delete associated tasks in Tasks table
        taskRepo.deleteAllByProjectId(projectId);
        LOGGER.info("ProjectService: deleteProject by Id: associated tasks deleted");
        // delete projects in Project_User join table
        projectUserRepo.deleteAllByProjectId(project.getId());
        LOGGER.info("ProjectService: deleteProject by Id: projects removed from Project_User join table");
        // delete projects in Project table
        //projectRepo.deleteAllByProjectId(project.getId());
        guestRepo.deleteByProjectId(projectId);
        projectRepo.deleteById(project.getId());
        LOGGER.info("ProjectService: deleteProject by Id: projects removed from Project table");
    }

    @Override
    public void deleteUserProjectsOnDeleteUser(User user) {
        // get list of projects for deletion where the user is the project OWNER
        Set<ProjectUser> usersOwnedProjects = projectUserRepo.findAllUsersProjects(user.getId(), Permissions.OWNER.getPermissionsNum());
        Set<Project> projects = usersOwnedProjects.stream().map(ProjectUser::getProject).collect(Collectors.toSet());
        projectUserRepo.deleteAllByUserId(user.getId());
        projects.forEach((project) -> taskRepo.deleteAllByProjectId(project.getId()));
        projects.forEach((project) -> projectRepo.deleteAllUserOwnedProjects(project.getId()));
    }

    // TODO remove
    @Override
    public List<OwnerProjectGuestsDTOOut> getGuestsOnOwnerProjects(String userEmail, long userId) {
        List<Project> test = projectRepo.findGuestsOnOwnedProjects(userEmail, userId);
        List<OwnerProjectGuestsDTOOut> ownerProjectsGuests = new ArrayList<>();
        for(Project project : test) {
            OwnerProjectGuestsDTOOut ownerProjectGuest = new OwnerProjectGuestsDTOOut();
            ownerProjectGuest.setProjectId(project.getId());
            ownerProjectGuest.setProjectTitle(project.getTitle());
            ownerProjectGuest.setDeadline(project.getDeadline());
            ownerProjectGuest.setGuestEmail(project.getProjectUsers().stream().map((User::getEmail)).toString());
            Permissions permissions = project.getGuestSet().stream().map(Guest::getPermissions).findAny().orElse(Permissions.OWNER);
            ownerProjectGuest.setGuestPermissions(permissions);
            ownerProjectsGuests.add(ownerProjectGuest);
        }
        return ownerProjectsGuests;
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
