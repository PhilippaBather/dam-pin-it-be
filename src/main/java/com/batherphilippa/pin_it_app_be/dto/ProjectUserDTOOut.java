package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Permissions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProjectUserDTOOut - defines the outgoing data transfer object for the ProjectUser join table
 * and includes the project name.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserDTOOut {

    private long projectId;
    private long userId;
    private Permissions permissions;
    private String title;
}
