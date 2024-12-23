package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * UserDTOIn - defines the data transfer object for an outgoing task object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailsDTOOut {

    private String title;
    private LocalDate deadline;
    private Priority priorityLevel;
    private int taskStatus;
    private int taskPosition;
}
