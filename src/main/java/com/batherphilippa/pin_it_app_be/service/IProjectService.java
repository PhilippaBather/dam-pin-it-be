package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.*;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.User;

import java.util.List;
import java.util.Set;

/**
 * IProjectService - Project Service interface defining the methods to be used.
 */
public interface IProjectService {

    Set<ProjectUserDTOOut> getAllUsersProjects(long userId) throws UserNotFoundException;
    ProjectAndPermissionsDTOOut getProjectById(long projectId, long userId) throws ProjectNotFoundException;
    Project getProjectByIdForGuest(long projectId, long userId) throws ProjectNotFoundException;
    Project getProjectGuestById(long projectId) throws ProjectNotFoundException;
    ProjectDTOOut saveProject(ProjectDTOIn projectDTOIn, User user) throws UserNotFoundException;
    Project save(Project project) throws ProjectNotFoundException;
    void updateProjectById(long projectId, String deadline, String description, String title) throws ProjectNotFoundException;
    void deleteProjectById(long projectId) throws ProjectNotFoundException;
    void deleteUserProjectsOnDeleteUser(User user);
    List<OwnerProjectGuestsDTOOut> getGuestsOnOwnerProjects(String userEmail, long userId);
}
