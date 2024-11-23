package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.repository.GuestRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuestService implements IGuestService {

    private final GuestRepo guestRepo;
    private final ProjectService projectService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public GuestService(GuestRepo guestRepo, ProjectService projectService, UserService userService, ModelMapper modelMapper) {
        this.guestRepo = guestRepo;
        this.projectService = projectService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Guest saveGuest(GuestDTOIn guestDTOIn) {
        //TODO: remove hard coding
        User user = userService.findById(1);
        Project project = projectService.getProjectByIdForGuest(1, 1);
        Guest guest = new Guest();
        // TODO: reset data base and classes with projectId = projectId and userId = userId so as to be able to use the model mapper
        guest.setEmail(guestDTOIn.getEmail());
        guest.setPermissions(guestDTOIn.getPermissions());
        guest.setUser(user);
        guest.setProject(project);
        return guestRepo.save(guest);
    }
}
