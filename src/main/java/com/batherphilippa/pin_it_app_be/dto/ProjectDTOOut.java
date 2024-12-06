package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectDTOIOut - defines the data transfer object for an outgoing project object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTOOut {
    private long projectId;
    private String title;
    private String description;
    private LocalDate deadline;
    private LocalDate creationDate;
    private ProjectStatus projectStatus;
    private List<GuestDTOOut> guestList = new ArrayList<>();
}
