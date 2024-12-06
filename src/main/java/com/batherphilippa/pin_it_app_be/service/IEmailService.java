package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.model.Email;
import jakarta.mail.SendFailedException;

public interface IEmailService {

    void sendEmail(Email details) throws SendFailedException;
}
