package com.batherphilippa.pin_it_app_be.dto;

import com.batherphilippa.pin_it_app_be.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
