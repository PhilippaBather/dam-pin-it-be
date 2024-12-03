package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * ProjectRepo - the Project Repository containing additional helper methods.
 */
@Repository
public interface ProjectRepo extends CrudRepository<Project, Long> {

    @Query(value = "SELECT pu.project_id, pu.user_id, pu.permissions, " +
            " p.title, p.description, p.created_on, p.deadline, p.project_status " +
            " FROM projects p " +
            " INNER JOIN project_user pu ON pu.project_id = p.project_id" +
            " WHERE pu.project_id = :projectId;", nativeQuery = true)
    Optional<Project> findByProjectId(long projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM projects p " +
                    " WHERE p.project_id = :projectId;", nativeQuery = true)
    void deleteAllByProjectId(long projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM projects p " +
            " WHERE p.project_id = :projectId;", nativeQuery = true)
    void deleteAllUserOwnedProjects(long projectId) throws UserNotFoundException;

    @Transactional
    @Modifying(clearAutomatically=true) // clears non-flushed values from the EntityManager
    @Query(value = "UPDATE projects p " +
            " SET p.deadline = :deadline, p.description = :description, p.title = :title " +
            " WHERE p.project_id = :projectId;", nativeQuery = true)
    void updateProjectByProjectId(long projectId, String deadline, String description, String title);

    @Query(value =  "SELECT " +
            "p.project_id, p.created_on, p.deadline, p.description, p.project_status, p.title, " +
            "pu.permissions, pu.user_id, " +
            "u.email as ownerEmail, u.first_name, u.surname," +
            "g.guest_id, g.email as guestEmail, g.permissions " +
            "FROM projects p " +
            "INNER JOIN project_user pu on pu.project_id = p.project_id " +
            "INNER JOIN users u on u.user_id = pu.user_id " +
            "LEFT JOIN guests g on g.project_id = pu.project_id " +
            "WHERE (pu.permissions != 0 AND g.email != :ownerEmail) " +
            "OR (pu.permissions = 0 AND pu.user_id = :ownerId);", nativeQuery = true)
    List<Project> findGuestsOnOwnedProjects(String ownerEmail, long ownerId);
}
