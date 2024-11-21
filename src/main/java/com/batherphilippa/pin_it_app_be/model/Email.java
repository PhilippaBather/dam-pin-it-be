package com.batherphilippa.pin_it_app_be.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String recipient;
    private String sender;
    private String subject;
    private String body;
}
