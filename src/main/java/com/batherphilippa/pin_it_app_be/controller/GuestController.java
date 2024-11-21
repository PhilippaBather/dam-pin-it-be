package com.batherphilippa.pin_it_app_be.controller;

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

@RestController
public class GuestController {


    @Value("${spring.mail.username=pabdevtest@gmail.com}")
    private String SENDER;
    private final String EMAIL_SUBJECT = "¡Pin-it App! Project Invitation";
    private final EmailService emailService;
    private final GuestService guestService;

    public GuestController(EmailService emailService, GuestService guestService) {
        this.emailService = emailService;
        this.guestService = guestService;
    }

    @PostMapping("/guest")
    public ResponseEntity<Guest> sendEmail(@Valid @RequestBody Guest guest) throws SendFailedException {
        Guest savedGuest = this.guestService.saveGuest(guest);
        String body = constructBody(guest);
        Email email = new Email(guest.getEmail(), SENDER, EMAIL_SUBJECT, body);
        this.emailService.sendEmail(email);
        return new ResponseEntity<>(savedGuest, HttpStatus.CREATED);
    }

    private String constructBody(Guest guest) {
        // TODO
        return null;
    }

}
