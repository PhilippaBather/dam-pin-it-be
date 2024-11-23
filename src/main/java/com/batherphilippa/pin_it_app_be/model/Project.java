package com.batherphilippa.pin_it_app_be.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.VALIDATION_PROJECT_TITLE_SIZE;

/**
 * Project - defines a project.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "projects")
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", updatable = false, nullable = false)
    private long id;

    @Size(min = 2, max = 50, message = VALIDATION_PROJECT_TITLE_SIZE)
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @DateTimeFormat
    @Column(name = "deadline")
    private LocalDate deadline;

    @CreationTimestamp
    @DateTimeFormat
    @Column(name = "created_on")
    LocalDate creationDate; // TODO: set at the BE

    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Guest> guestSet = new HashSet<>();

    @ManyToMany(mappedBy = "projectsSet")
    @EqualsAndHashCode.Exclude
    private Set<User> projectUsers = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<ProjectUser> userProjectsPermissions = new HashSet<>();



}
