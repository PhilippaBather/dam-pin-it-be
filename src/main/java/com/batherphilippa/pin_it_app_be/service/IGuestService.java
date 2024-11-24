package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.User;

public interface IGuestService {

    Guest saveGuest(GuestDTOIn guest, User user, Project project);
    boolean updateGuestDetails(User user);
}
