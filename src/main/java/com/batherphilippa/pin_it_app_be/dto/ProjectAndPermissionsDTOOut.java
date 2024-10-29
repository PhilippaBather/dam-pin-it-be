package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Permissions;
import com.batherphilippa.pin_it_app_be.model.Status;

import java.time.LocalDate;

/**
 * ProjectDTOOut - defines the data transfer object for an outgoing project object with
 * permissions included.
 */
public class ProjectAndPermissionsDTOOut {

    private long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private LocalDate creationDate;
    private Status projectStatus;
    private Permissions permissions;
}
