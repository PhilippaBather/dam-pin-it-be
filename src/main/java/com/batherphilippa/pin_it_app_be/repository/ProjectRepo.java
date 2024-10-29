package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.dto.ProjectUserDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * ProjectRepo - the Project Repository containing additional helper methods.
 */
@Repository
public interface ProjectRepo extends CrudRepository<Project, Long> {

    @Query(value =
            "SELECT pu.project_user_id as projectId, pu.user_project_id as userId," +
                    " pu.permissions, p.name FROM project_user pu" +
                    "INNER JOIN projects p ON p.project_id = pu.project_user_id" +
                    "WHERE pu.project_user_id = :projectId" +
                    "AND pu.user_project_id = : userId", nativeQuery = true)
    Optional<Project> findProjectById(long projectId) throws ProjectNotFoundException;

    @Query(value =
            "SELECT pu.project_user_id as projectId, pu.user_project_id as userId," +
                    " pu.permissions, p.name FROM project_user pu" +
            "INNER JOIN projects p ON p.project_id = pu.project_user_id" +
                    "WHERE pu.user_project_id = : userId", nativeQuery = true)
    Set<ProjectUserDTOOut> findAllUsersProjects(long userId) throws UserNotFoundException;

}
