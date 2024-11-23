package com.batherphilippa.pin_it_app_be.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.VALIDATION_EMAIL_NOT_BLANK;

/**
 * Guest - defines a person invited by a user object to work on a project.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id", updatable = false, nullable = false)
    private long guestId;

    @NotBlank(message = VALIDATION_EMAIL_NOT_BLANK)
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "permissions")
    private Permissions permissions;

    @ManyToOne
    @JsonBackReference(value = "guest_projects")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonBackReference(value = "guest_project")
    @JoinColumn(name = "project_id")
    private Project project;

}
