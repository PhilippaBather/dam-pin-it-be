package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.dto.ProjectDTOOut;
import com.batherphilippa.pin_it_app_be.dto.SharedProjectsDTOOut;
import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.Permissions;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGuestService {

    Guest saveGuest(GuestDTOIn guest, User user, Project project);
    Map<Project, Permissions> updateGuestDetails(String email);
    void updateNotifiedStatus(String email);
    Set<Long> getGuestProjectIds(User user);
    Set<SharedProjectsDTOOut> getSharedProjects(String email);
    List<ProjectDTOOut> getOwnedProjects(long userId);

    void deleteGuest(long projectId, User user);
}
