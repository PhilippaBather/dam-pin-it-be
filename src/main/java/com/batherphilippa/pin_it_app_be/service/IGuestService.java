package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.dto.ProjectDTOOut;
import com.batherphilippa.pin_it_app_be.dto.SharedProjectsDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.GuestNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.Permissions;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGuestService {

    Guest saveGuest(GuestDTOIn guest, User user, Project project) throws UserNotFoundException, ProjectNotFoundException;
    Map<Project, Permissions> updateGuestDetails(String email) throws GuestNotFoundException;
    void updateNotifiedStatus(String email) throws GuestNotFoundException;
    Set<Long> getGuestProjectIds(User user) throws UserNotFoundException;
    Set<SharedProjectsDTOOut> getSharedProjects(String email);
    List<ProjectDTOOut> getOwnedProjects(long userId) throws UserNotFoundException;
    Guest updateGuestPermissions(GuestDTOIn guestDTOIn) throws ProjectNotFoundException, GuestNotFoundException, UserNotFoundException;
    void deleteGuest(long projectId, User user) throws ProjectNotFoundException;
    void deleteGuest(long projectId, String guestEmail) throws ProjectNotFoundException, GuestNotFoundException;
}
