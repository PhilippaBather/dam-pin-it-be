package com.batherphilippa.pin_it_app_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOOut {

    private long id;
    private String username;
    private String forename;
    private String surname;
    private String email;

}
