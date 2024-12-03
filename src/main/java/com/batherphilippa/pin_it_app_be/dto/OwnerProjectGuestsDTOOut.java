package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Permissions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerProjectGuestsDTOOut {
    private long projectId;
    private String projectTitle;
    private LocalDate deadline;
    private String guestEmail;
    private Permissions guestPermissions;
}
