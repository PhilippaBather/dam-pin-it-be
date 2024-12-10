package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.model.Email;
import jakarta.mail.SendFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;
    @Override
    public void sendEmail(Email details) throws SendFailedException {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(details.getSender());
        msg.setTo(details.getRecipient());
        msg.setText(details.getBody());
        msg.setSubject(details.getSubject());

        emailSender.send(msg);

        logger.info(this.getClass().getName() + "Email sent.");
    }
}
