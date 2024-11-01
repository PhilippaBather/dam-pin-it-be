package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.model.Project;
import com.batherphilippa.pin_it_app_be.model.ProjectUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ProjectRepo - the Project Repository containing additional helper methods.
 */
@Repository
public interface ProjectRepo extends CrudRepository<Project, Long> {

    @Query(value = "SELECT pu.project_id, pu.user_id, pu.permissions," +
            " p.title, p.description, p.created_on, p.deadline, p.project_status " +
            " FROM projects p " +
            " INNER JOIN project_user pu ON pu.project_id = p.project_id" +
            " WHERE pu.project_id = :projectId;", nativeQuery = true)
    Optional<Project> findByProjectId(long projectId);
}
