package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Permissions;
import com.batherphilippa.pin_it_app_be.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * ProjectDTOOut - defines the data transfer object for an outgoing project object with
 * permissions included.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAndPermissionsDTOOut {

    private long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private LocalDate creationDate;
    private Status projectStatus;
    private Permissions permissions;
}
