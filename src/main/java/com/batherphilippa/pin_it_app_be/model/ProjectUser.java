package com.batherphilippa.pin_it_app_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProjectUser - class maps the join table ProjectUserKey.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "project_user")
@Table(name = "project_user")
public class ProjectUser {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ProjectUserKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    // TODO: enum validation
    @Column(name = "permissions")
    private Permissions permissions;

    public ProjectUser(Project project, User user, Permissions permissions) {
        this.project = project;
        this.user = user;
        this.permissions = permissions;
    }
}
