package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.ProjectAndPermissionsDTOOut;
import com.batherphilippa.pin_it_app_be.dto.ProjectDTOIn;
import com.batherphilippa.pin_it_app_be.dto.ProjectDTOOut;
import com.batherphilippa.pin_it_app_be.dto.ProjectUserDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;

import java.util.Set;

/**
 * IProjectService - Project Service interface defining the methods to be used.
 */
public interface IProjectService {

    Set<ProjectUserDTOOut> getAllUsersProjects(long userId) throws UserNotFoundException;
    ProjectAndPermissionsDTOOut getProjectById(long projectId) throws ProjectNotFoundException;
    ProjectDTOOut saveProject(ProjectDTOIn projectDTOIn);
    ProjectDTOOut updateProjectById(long projectId, ProjectDTOIn project) throws ProjectNotFoundException;
    void deleteProjectById(long projectId) throws ProjectNotFoundException;
}
