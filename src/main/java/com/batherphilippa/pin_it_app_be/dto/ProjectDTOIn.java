package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Status;

import java.time.LocalDate;

/**
 * ProjectDTOIn - defines the data transfer object for an incoming project object.
 */
public class ProjectDTOIn {
    private String title;
    private String description;
    private LocalDate deadline;
    private Status projectStatus;

}
