package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.config.AppConfig;
import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.dto.GuestDTOOut;
import com.batherphilippa.pin_it_app_be.dto.ProjectDTOOut;
import com.batherphilippa.pin_it_app_be.dto.SharedProjectsDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Email;
import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.service.EmailService;
import com.batherphilippa.pin_it_app_be.service.GuestService;
import com.batherphilippa.pin_it_app_be.service.ProjectService;
import com.batherphilippa.pin_it_app_be.service.UserService;
import jakarta.mail.SendFailedException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Set;

@RestController
public class GuestController {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private final String SENDER;
    private final String LOCAL_HOST;
    private final EmailService emailService;
    private final GuestService guestService;
    private final ProjectService projectService;
    private final UserService userService;

    public GuestController(@Value("${spring.mail.username}") String sender,  @Value("${http.origins}") String localHost,  EmailService emailService, GuestService guestService, ProjectService projectService, UserService userService) {
        this.emailService = emailService;
        this.guestService = guestService;
        this.projectService = projectService;
        this.userService = userService;
        this.SENDER = sender;
        this.LOCAL_HOST = localHost;
    }

    @PostMapping("/guests")
    public ResponseEntity<Guest> handleGuestInvitation(@Valid @RequestBody GuestDTOIn guestDTOIn) throws SendFailedException, HttpClientErrorException.UnprocessableEntity {
        // save guest
        User user = userService.findById(guestDTOIn.getUserId());
        Project project = projectService.getProjectByIdForGuest(guestDTOIn.getProjectId(), guestDTOIn.getUserId());
        Guest savedGuest = this.guestService.saveGuest(guestDTOIn, user, project);

        // notify user via email
        final String EMAIL_SUBJECT = "¡Pin-it App! Project Invitation";
        String body = constructEmailBodyInvite(guestDTOIn.getBody(), user.getForename(), user.getSurname(), project.getTitle(), savedGuest.getPermissions().getPermissionsDescription());
        sendEmail(guestDTOIn.getEmail(), EMAIL_SUBJECT, body);
        return new ResponseEntity<>(savedGuest, HttpStatus.CREATED);
    }

    @GetMapping("/guests/shared-projects/{userId}")
    public ResponseEntity<Set<SharedProjectsDTOOut>> getSharedProjects(@PathVariable long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        Set<SharedProjectsDTOOut> sharedProjectsSet = guestService.getSharedProjects(user.getEmail());
        return new ResponseEntity<>(sharedProjectsSet, HttpStatus.OK);
    }
    @DeleteMapping("/guests/{userId}/shared-projects/{projectId}")
    public ResponseEntity<Void> removeSharedProject(@PathVariable long projectId, @PathVariable long userId) throws UserNotFoundException, ProjectNotFoundException {
        User user = userService.findById(userId);
        guestService.deleteGuest(projectId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/guests/owned-projects/{userId}")
    public ResponseEntity<List<ProjectDTOOut>> getOwnedProjects(@PathVariable long userId) {
        User user = userService.findById(userId);
        List<ProjectDTOOut> ownerGuestProjects = guestService.getOwnedProjects(user.getId());
        return new ResponseEntity<>(ownerGuestProjects, HttpStatus.OK);
    }

    @PutMapping("/guests/owned-projects")
    public ResponseEntity<GuestDTOOut> updateGuestPermissions(@Valid @RequestBody GuestDTOIn guestDTOIn) throws SendFailedException {
        logger.info("GuestController: updateGuestPermissions");
        final String EMAIL_SUBJECT = "¡Pin-it App! Project Permissions Update";
        GuestDTOOut updatedGuest = guestService.updateGuestPermissions(guestDTOIn);
        String body = constructEmailBodyUpdate(guestDTOIn.getBody(), updatedGuest.getPermissions().getPermissionsDescription(), updatedGuest.getProjectTitle());
        sendEmail(updatedGuest.getEmail(), EMAIL_SUBJECT, body);
        return new ResponseEntity<>(updatedGuest, HttpStatus.OK);
    }
    @DeleteMapping("/guests/owned-projects/guest/{guestEmail}/project/{projectId}")
    public ResponseEntity<Void> deleteGuestFromOwnedProject(@PathVariable String guestEmail, @PathVariable long projectId) {
        logger.info("GuestController: deleteGuestFromOwnedProject");
        guestService.deleteGuest(projectId, guestEmail);
        return ResponseEntity.noContent().build();
    }

    private void sendEmail(String recipient, String subject, String body) throws SendFailedException {
        Email email = new Email(recipient, SENDER, subject, body);
        this.emailService.sendEmail(email);
    }
    private String constructEmailBodyInvite(String body, String forename, String surname, String projectTitle, String permissions) {
        String username = forename.concat(" ").concat(surname);
        String msg = String.format("%s has granted you %s on the project '%s'.  Register or login to get started on the project at %s", username, permissions.toLowerCase(), projectTitle, LOCAL_HOST);
        return constructBody(body, msg);
    }

    private String constructEmailBodyUpdate(String body, String permissions, String projectTitle) {
        String msg = String.format("Your permissions have been updated to %s on project '%s'.  Log in to continue working on the project at %s", permissions.toLowerCase(), projectTitle, LOCAL_HOST);
        return constructBody(body, msg);
    }

    private String constructBody(String body, String msg) {
        if(body.trim().length() > 0) {
            return String.format("%s\n\n%s", msg, body.trim());
        }
        return msg;
    }
}
