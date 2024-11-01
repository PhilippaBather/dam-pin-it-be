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

    @Column(name = "p_id")
    private long pId;

    @Column(name = "u_id")
    private long uId;

    // Embedded type overrides equals and hash code

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ProjectUserId that = (ProjectUserId) o;
        return Objects.equals(pId, that.pId) &&
                Objects.equals(uId, that.uId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pId, uId);
    }
}
