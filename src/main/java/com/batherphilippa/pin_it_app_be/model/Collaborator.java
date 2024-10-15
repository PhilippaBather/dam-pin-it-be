package com.batherphilippa.pin_it_app_be.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.VALIDATION_COLLABORATOR_EMAIL;
import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.VALIDATION_COLLABORATOR_ROLE;

/**
 * Collaborator - define un participante invitado para collaborar en un proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "collaborators")
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collaborator_id", updatable = false, nullable = false)
    private long id;

    @NotBlank(message = VALIDATION_COLLABORATOR_EMAIL)
    @Email
    @Column
    private String email;

    @NotBlank(message = VALIDATION_COLLABORATOR_ROLE)
    @Column
    private Role role;

    @ManyToOne
    @JsonBackReference(value = "projects_collaborators")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JsonBackReference(value = "user_collaborator")
    @JoinColumn(name = "user_email")
    private User user;
}
