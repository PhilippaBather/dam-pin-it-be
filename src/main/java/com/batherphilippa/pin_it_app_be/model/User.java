package com.batherphilippa.pin_it_app_be.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.*;
import static com.batherphilippa.pin_it_app_be.constants.ValidationRegex.VALIDATION_PASSWORD_REGEX;

/**
 * User - define un usuario de la aplicación.
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

    @NotBlank(message = VALIDATION_USERNAME_NOT_BLANK)
    @Size(min = 2, max = 15, message = VALIDATION_USERNAME_SIZE)
    @Column(name = "username")
    private String username;

    @NotBlank(message = VALIDATION_FORENAME_NOT_BLANK)
    @Size(min = 2, max = 50, message = VALIDATION_FORENAME_SIZE)
    @Column(name = "first_name")
    private String forename;

    @NotBlank(message = VALIDATION_SURNAME_NOT_BLANK)
    @Size(min = 2, max = 100, message = VALIDATION_SURNAME_SIZE)
    @Column(name = "surname)")
    private String surname;

    @NotBlank(message = VALIDATION_EMAIL_NOT_BLANK)
    @Email
    @Column(name = "email")
    String email;

    // contraseña debe incluir 1 número y un carácter especial con tamaño mínimo de 8 carácteres y máximo de 25
    @Pattern(regexp = VALIDATION_PASSWORD_REGEX, message = VALIDATION_PASSWORD_REGEX_CONSTRAINTS)
    @NotBlank(message = VALIDATION_PASSWORD_NOT_BLANK)
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> ownedProjects;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Collaborator> collaborativeProjects;
}
