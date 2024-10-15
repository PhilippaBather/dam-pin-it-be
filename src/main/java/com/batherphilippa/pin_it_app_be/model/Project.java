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
import java.util.Set;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.*;

/**
 * Project - define un proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", updatable = false, nullable = false)
    private long id;

    @NotBlank(message = VALIDATION_PROJECT_TITLE_NOT_BLANK)
    @Size(min = 2, max = 50, message = VALIDATION_PROJECT_TITLE_SIZE)
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @NotBlank(message = VALIDATION_PROJECT_DEADLINE)
    @DateTimeFormat
    @Column(name = "deadline")
    LocalDate deadline;

    // "CURRENT" por defecto
    @Column(name = "project_status")
    private Status projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Collaborator> collaborators;

    @ManyToOne
    @JsonBackReference(value = "user_owned_projects")
    @JoinColumn(name = "user_id")
    private User user;

}
