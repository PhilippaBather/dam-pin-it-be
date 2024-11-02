package com.batherphilippa.pin_it_app_be.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * ProjectUserId - the join table for the entity ProjectUser.  Note: the @Embedded type requires Serializable.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProjectUserId implements Serializable {

    @Column(name = "project_id")
    private long projectId;

    @Column(name = "user_id")
    private long userId;

    // Embedded type overrides equals and hash code

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ProjectUserId that = (ProjectUserId) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, userId);
    }
}
