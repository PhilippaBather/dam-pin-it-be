package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Permissions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedProjectsDTOOut {
    private String guestEmail;
    private boolean isNotified;
    private Permissions permissions;
    private long projectId;
    private String projectTitle;
    private LocalDate deadline;
    private String ownerName;
    private String ownerSurname;
    private String ownerEmail;
    private long ownerId;
}
