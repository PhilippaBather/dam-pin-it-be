package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Status;

import java.time.LocalDate;

/**
 * ProjectDTOIOut - defines the data transfer object for an outgoing project object.
 */
public class ProjectDTOOut {
    private long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private LocalDate creationDate;
    private Status projectStatus;
}
