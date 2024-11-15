package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * ProjectDTOIn - defines the data transfer object for an incoming project object.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTOIn {
    private String title;
    private String description;
    private LocalDate deadline;
    private ProjectStatus projectStatus;

}
