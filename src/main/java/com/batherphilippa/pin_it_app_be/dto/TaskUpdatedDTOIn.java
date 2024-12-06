package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Priority;
import com.batherphilippa.pin_it_app_be.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * TaskUpdatedDTOIn - defines the data transfer object for an incoming task object to be modified.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdatedDTOIn {

    private long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private Priority priorityLevel;
    private TaskStatus taskStatus;
    private int taskPosition;
    private long projectId;
}
