package com.batherphilippa.pin_it_app_be.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.*;

/**
 * Task - define una tarea de un proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", updatable = false, nullable = false)
    private long id;

    @NotBlank(message = VALIDATION_TASK_TITLE_NOT_BLANK)
    @Size(min = 2, max = 50, message = VALIDATION_TASK_TITLE_SIZE)
    @Column(name = "task_title")
    private String title;

    @NotBlank(message = VALIDATION_TASK_DESCRIPTION_NOT_BLANK)
    @Column(name = "task_description")
    private String description;

    @DateTimeFormat
    @Column(name = "task_deadline")
    private LocalDate deadline;

    // none por defecto si un nivel no está asignado
    @Column(name = "priority_level")
    private Priority priorityLevel;

    @ManyToOne
    @JsonBackReference(value = "projects_tasks")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JsonBackReference(value = "collaborator_tasks")
    private Collaborator collaborator;

}
