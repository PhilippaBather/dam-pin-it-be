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

import java.util.Optional;

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

        // check to see if guest is a registered user
        //UserDTOOut guestUser = userService.findByEmail(guestDTOIn.getEmail());
        Optional<User> guestUser = userRepo.findByEmail(guestDTOIn.getEmail());
        // if registered, add project to user's projects
        if (guestUser.isPresent()) {
            ProjectUser projectUser = new ProjectUser(project, guestUser.get(), guestDTOIn.getPermissions());
            projectUserRepo.save(projectUser);
            guestUser.get().getProjectsSet().add(project);
            user.getGuests().add(guest);
        }

        // if not registered, handle above on registration

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
    public boolean updateGuestDetails(User user) {
        Optional<Guest> guest = guestRepo.findByEmail(user.getEmail());
        if(guest.isPresent() && !guest.get().isNotified()) {
            guest.get().setNotified(true);
            Guest savedGuest = guestRepo.save(guest.get());
            setNewProjectUser(guest.get().getProject(), user, guest.get().getPermissions());
            return true;
        }
        return false;
    }

    private void setNewProjectUser(Project project, User user, Permissions permissions) {
        user.getProjectsSet().add(project);
        userRepo.save(user);
        ProjectUser projectUser = projectUserRepo.findProjectUserByProjectIdAndUserId(project.getId(), user.getId());
        projectUser.setPermissions(permissions);
        projectUserRepo.save(projectUser);
    }
}
