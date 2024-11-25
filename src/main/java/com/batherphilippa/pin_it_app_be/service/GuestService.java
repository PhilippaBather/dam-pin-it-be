package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.exceptions.GuestInvitationException;
import com.batherphilippa.pin_it_app_be.model.*;
import com.batherphilippa.pin_it_app_be.repository.GuestRepo;
import com.batherphilippa.pin_it_app_be.repository.ProjectRepo;
import com.batherphilippa.pin_it_app_be.repository.ProjectUserRepo;
import com.batherphilippa.pin_it_app_be.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GuestService implements IGuestService {
    private static final Logger logger = LoggerFactory.getLogger(GuestService.class);

    private final GuestRepo guestRepo;
    private final ProjectRepo projectRepo;
    private final ProjectUserRepo projectUserRepo;
    private final UserRepo userRepo;

    public GuestService(GuestRepo guestRepo, ProjectRepo projectRepo, ProjectUserRepo projectUserRepo, UserRepo userRepo) {
        this.guestRepo = guestRepo;
        this.projectRepo = projectRepo;
        this.projectUserRepo = projectUserRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Guest saveGuest(GuestDTOIn guestDTOIn, User user, Project project) throws GuestInvitationException {
        // check to see if invitation previously sent
        Optional<Guest> invitedGuest = guestRepo.findByEmailAndProjectId(guestDTOIn.getEmail(), project.getId());
        if (invitedGuest.isPresent()) {
            throw new GuestInvitationException(guestDTOIn.getEmail(), project.getId());
        }
        Guest guest = setGuest(guestDTOIn, user, project);

        return guestRepo.save(guest);
    }

    private Guest setGuest(GuestDTOIn guestDTOIn, User user, Project project) {
        // TODO: reset data base and classes with projectId = projectId and userId = userId so as to be able to use the model mapper
        Guest guest = new Guest();
        guest.setEmail(guestDTOIn.getEmail());
        guest.setPermissions(guestDTOIn.getPermissions());
        guest.setUser(user);
        guest.setProject(project);
        return guest;
    }

    @Override
    public Map<Project, Permissions> updateGuestDetails(String email) {
        List<Guest> guestList = guestRepo.findByEmail(email);
        return guestList.stream().filter(guest -> !guest.isNotified()).collect(Collectors.toMap(Guest::getProject, Guest::getPermissions));
    }

    @Override
    public void updateNotifiedStatus(String email) {
        List<Guest> guestList = guestRepo.findByEmail(email);
        guestList.forEach(guest -> {
            guest.setNotified(true);
            guestRepo.save(guest);
        });
    }

    @Override
    public Set<Long> getGuestProjectIds(User user) {
        List<Guest> guestList = guestRepo.findByEmail(user.getEmail());
        return guestList.stream().filter(guest -> !guest.isNotified()).map(Guest::getProject).map(Project::getId).collect(Collectors.toSet());
    }

}
