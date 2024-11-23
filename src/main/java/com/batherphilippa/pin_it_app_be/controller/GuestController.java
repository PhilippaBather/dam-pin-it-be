package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.model.Email;
import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.service.EmailService;
import com.batherphilippa.pin_it_app_be.service.GuestService;
import jakarta.mail.SendFailedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class GuestController {

    private final String SENDER;
    private final EmailService emailService;
    private final GuestService guestService;

    public GuestController(@Value("${spring.mail.username}") String sender, EmailService emailService, GuestService guestService) {
        this.emailService = emailService;
        this.guestService = guestService;
        this.SENDER = sender;
    }

    @PostMapping("/guests")
    public ResponseEntity<Guest> handleGuestInvitation(@Valid @RequestBody GuestDTOIn guestDTOIn) throws SendFailedException, HttpClientErrorException.UnprocessableEntity {
        Guest savedGuest = this.guestService.saveGuest(guestDTOIn);
        sendEmail(guestDTOIn);
        return new ResponseEntity<>(savedGuest, HttpStatus.CREATED);
    }

    private ResponseEntity<Email> sendEmail(GuestDTOIn guestDTOIn) throws SendFailedException {
        final String EMAIL_SUBJECT = "¡Pin-it App! Project Invitation";
        String body = constructBody(guestDTOIn);
        Email email = new Email(guestDTOIn.getEmail(), SENDER, EMAIL_SUBJECT, body);
        this.emailService.sendEmail(email);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }
    private String constructBody(GuestDTOIn guest) {
        // TODO
        return "Hello AGAIN, world!";
    }

}
