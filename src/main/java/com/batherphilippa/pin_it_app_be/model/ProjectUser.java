package com.batherphilippa.pin_it_app_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProjectUser - class maps the join table ProjectUserId.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ProjectUser")
public class ProjectUser {

    @EmbeddedId
    private ProjectUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_user_id", referencedColumnName = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_project_id", referencedColumnName = "user_id")
    private User user;

    // TODO: enum validation
    @Column(name = "permissions")
    private Permissions permissions;

    public ProjectUser(User user, Project project, Permissions permissions) {
        this.user = user;
        this.project = project;
        this.id = new ProjectUserId(user.getId(), project.getId());
        this.permissions = permissions;
    }
}
