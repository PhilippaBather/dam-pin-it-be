package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.controller.ProjectController;
import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.dto.GuestDTOOut;
import com.batherphilippa.pin_it_app_be.dto.ProjectDTOOut;
import com.batherphilippa.pin_it_app_be.dto.SharedProjectsDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.GuestInvitationException;
import com.batherphilippa.pin_it_app_be.exceptions.GuestNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.*;
import com.batherphilippa.pin_it_app_be.repository.GuestRepo;
import com.batherphilippa.pin_it_app_be.repository.ProjectRepo;
import com.batherphilippa.pin_it_app_be.repository.ProjectUserRepo;
import com.batherphilippa.pin_it_app_be.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GuestService implements IGuestService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ModelMapper modelMapper;
    private final GuestRepo guestRepo;
    private final ProjectRepo projectRepo;
    private final ProjectUserRepo projectUserRepo;
    private final UserRepo userRepo;

    public GuestService(GuestRepo guestRepo, ProjectRepo projectRepo, ProjectUserRepo projectUserRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.guestRepo = guestRepo;
        this.projectRepo = projectRepo;
        this.projectUserRepo = projectUserRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
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

    @Override
    public Set<SharedProjectsDTOOut> getSharedProjects(String email) {
        Set<Guest> guestSet = guestRepo.findSharedProjectsByGuestEmail(email);
        Set<SharedProjectsDTOOut> sharedProjectsSet = new HashSet<>();
        for (Guest guest : guestSet) {
            SharedProjectsDTOOut sharedProject = new SharedProjectsDTOOut();
            sharedProject.setGuestEmail(guest.getEmail());
            sharedProject.setNotified(guest.isNotified());
            sharedProject.setPermissions(guest.getPermissions());
            sharedProject.setProjectId(guest.getProject().getId());
            sharedProject.setProjectTitle(guest.getProject().getTitle());
            sharedProject.setDeadline(guest.getProject().getDeadline());
            sharedProject.setOwnerName(guest.getUser().getForename());
            sharedProject.setOwnerSurname(guest.getUser().getSurname());
            sharedProject.setOwnerEmail(guest.getUser().getEmail());
            sharedProject.setOwnerId(guest.getUser().getId());
            sharedProjectsSet.add(sharedProject);
        }
        return sharedProjectsSet;
    }

    @Override
    public GuestDTOOut updateGuestPermissions(GuestDTOIn guestDTOIn) throws ProjectNotFoundException, GuestNotFoundException, UserNotFoundException {
        logger.info("GuestService: updateGuestPermissions");
        Project project = projectRepo.findById(guestDTOIn.getProjectId()).orElseThrow(() -> new ProjectNotFoundException(guestDTOIn.getProjectId()));
        guestRepo.updateGuestPermissions(guestDTOIn.getPermissions(), guestDTOIn.getEmail(), guestDTOIn.getProjectId());
        updateProjectUserPermissions(guestDTOIn, project.getId());
        return buildGuestDTO(guestDTOIn.getEmail(), project.getTitle(), project.getId());
    }
    private void updateProjectUserPermissions(GuestDTOIn guestDTOIn, long projectId) {
        User user = userRepo.findByEmail(guestDTOIn.getEmail()).orElseThrow(() -> new UserNotFoundException(guestDTOIn.getEmail()));
        ProjectUser projectUser = projectUserRepo.findProjectUserByProjectIdAndUserId(projectId, user.getId());
        if (projectUser != null) {
            projectUser.setPermissions(guestDTOIn.getPermissions());
        }
    }

    private GuestDTOOut buildGuestDTO(String guestEmail, String projectTitle, long projectId) {
        GuestDTOOut guestDTOOut = new GuestDTOOut();
        Guest guest = guestRepo.findByEmailAndProjectId(guestEmail, projectId).orElseThrow(() -> new GuestNotFoundException(guestEmail));
        modelMapper.map(guest, guestDTOOut);
        guestDTOOut.setProjectTitle(projectTitle);
        return guestDTOOut;
    }

    @Override
    public void deleteGuest(long projectId, User user) throws ProjectNotFoundException {
        // delete guests who are registered users
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        guestRepo.deleteByProjectIdAndGuestEmail(project.getId(), user.getEmail());
        projectUserRepo.deleteByUserIdAndProjectId(user.getId(), projectId);
        // delete guests who have yet to register

    }
    @Override
    public void deleteGuest(long projectId, String guestEmail) throws ProjectNotFoundException {
        logger.info("GuestService: deleteGuest");
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        guestRepo.deleteByProjectIdAndGuestEmail(project.getId(), guestEmail);
        Optional<User> user = userRepo.findByEmail(guestEmail);
        user.ifPresent(value -> projectUserRepo.deleteByUserIdAndProjectId(value.getId(), projectId));
    }

    @Override
    public List<ProjectDTOOut> getOwnedProjects(long userId) {
        List<ProjectDTOOut> projectDTOOutList = getProjects(userId);
        return addGuests(projectDTOOutList, userId);
    }

    private List<ProjectDTOOut> getProjects(long userId) {
        Set<ProjectUser> projectUserSet = projectUserRepo.findAllUsersProjects(userId);
        Set<ProjectUser> filteredProjectUserSet = projectUserSet.stream().filter(pu -> pu.getPermissions() == Permissions.OWNER).collect(Collectors.toSet());
        List<ProjectDTOOut> projectDTOOutList = new ArrayList<>();
        for(ProjectUser pu : filteredProjectUserSet) {
            Optional<Project> projectOptional = projectRepo.findById(pu.getProject().getId());
            if(projectOptional.isPresent()) {
                ProjectDTOOut projectDTOOut = new ProjectDTOOut();
                modelMapper.map(projectOptional.get(), projectDTOOut);
                projectDTOOutList.add(projectDTOOut);
            }
        }
        return projectDTOOutList;
    }

    private List<ProjectDTOOut> addGuests(List<ProjectDTOOut> projectDTOOutList, long userId) {
        List<Guest> guestList = guestRepo.findOwnedProjectsWithGuests(userId);
        for (int i = 0; i < guestList.size(); i++) {
            for(int j = 0; j < projectDTOOutList.size(); j++) {
                long pid = projectDTOOutList.get(j).getProjectId();
                long gpid = guestList.get(i).getProject().getId();
                if(pid == gpid) {
                    GuestDTOOut guestDTOOut = new GuestDTOOut();
                    modelMapper.map(guestList.get(i), guestDTOOut);
                    ProjectDTOOut project = projectDTOOutList.get(j);
                    project.getGuestList().add(guestDTOOut);
                }
            }
        }
        return projectDTOOutList;
    }
}
