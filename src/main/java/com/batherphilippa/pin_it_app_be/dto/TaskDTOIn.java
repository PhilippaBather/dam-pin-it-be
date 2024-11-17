package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * TaskDTOIn - defines the data transfer object for an incoming task object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTOIn {

    private String title;
    private String description;
    private LocalDate deadline;
    private Priority priorityLevel;
    private int taskStatus;
    private int taskPosition;
    private long projectId;
}
