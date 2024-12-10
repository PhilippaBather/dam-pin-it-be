package com.batherphilippa.pin_it_app_be.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.*;

/**
 * User - defines a registered user of the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private long id;

    @NotBlank(message = VALIDATION_FORENAME_NOT_BLANK)
    @Size(min = 2, max = 50, message = VALIDATION_FORENAME_SIZE)
    @Column(name = "first_name")
    private String forename;

    @NotBlank(message = VALIDATION_SURNAME_NOT_BLANK)
    @Size(min = 2, max = 100, message = VALIDATION_SURNAME_SIZE)
    @Column(name = "surname")
    private String surname;

    @NotBlank(message = VALIDATION_EMAIL_NOT_BLANK)
    @Email
    @Column(name = "email")
    String email;

    @NotBlank(message = VALIDATION_PASSWORD_NOT_BLANK)
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Guest> guestSet = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projectsSet = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectUser> userProjectsPermissions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskComment> taskCommentList = new ArrayList<>();
}
