package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.Permissions;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.User;

import java.util.Map;
import java.util.Set;

public interface IGuestService {

    Guest saveGuest(GuestDTOIn guest, User user, Project project);
    Set<Long> getGuestProjectIds(User user);
    Map<Project, Permissions> updateGuestDetails(String email);

    void updateNotifiedStatus(String email);
}
