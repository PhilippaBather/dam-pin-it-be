package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Permissions;

/**
 * ProjectUserDTOOut - defines the outgoing data transfer object for the ProjectUser join table
 * and includes the project name.
 */
public class ProjectUserDTOOut {
    private long id;
    private long projectId;
    private long userId;
    private Permissions permissions;
    private String name;
}
