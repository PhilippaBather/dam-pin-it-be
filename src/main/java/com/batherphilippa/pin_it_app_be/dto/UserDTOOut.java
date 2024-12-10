package com.batherphilippa.pin_it_app_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * UserDTOOut - defines the data transfer object for an outgoing user object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOOut {

    private long id;
    private String forename;
    private String surname;
    private String email;
    private Set<Long> projectNotifications = new HashSet<>();
}
