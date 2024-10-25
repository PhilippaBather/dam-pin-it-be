package com.batherphilippa.pin_it_app_be.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.Set;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.*;
import static com.batherphilippa.pin_it_app_be.constants.ValidationRegex.VALIDATION_PASSWORD_REGEX;

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
    @Column(name = "surname)")
    private String surname;

    @NotBlank(message = VALIDATION_EMAIL_NOT_BLANK)
    @Email
    @Column(name = "email")
    String email;

    // password must include at least one character, one uppercase character, and one special character
    // password must be between 8 and 25 characters inclusive
    @Pattern(regexp = VALIDATION_PASSWORD_REGEX, message = VALIDATION_PASSWORD_REGEX_CONSTRAINTS)
    @NotBlank(message = VALIDATION_PASSWORD_NOT_BLANK)
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Guest> guests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectUser> userProjects;

    public void addProject(Project project, Role role) {
        ProjectUser projectUser = new ProjectUser(this, project, role);
        userProjects.add(projectUser);
        project.getProjectUsers().add(projectUser);
    }

    public void removeProject(Project project) {
        for (Iterator<ProjectUser> iterator = userProjects.iterator();
             iterator.hasNext(); ) {
            ProjectUser projectUser = iterator.next();

            if (projectUser.getUser().equals(this) &&
                    projectUser.getProject().equals(project)) {
                iterator.remove();
                projectUser.getProject().getProjectUsers().remove(projectUser);
                projectUser.setUser(null);
                projectUser.setProject(null);
            }
        }
    }


}
