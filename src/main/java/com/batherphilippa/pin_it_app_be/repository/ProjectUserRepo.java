package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.ProjectUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * ProjectUserRepo - the ProjectUser Repository containing additional helper methods.
 */
@Repository
public interface ProjectUserRepo extends CrudRepository<ProjectUser, Long> {

    ProjectUser findProjectUserByProjectIdAndUserId(long projectId, long userId);
    @Query(value = "SELECT pu.project_id, pu.user_id, pu.permissions," +
            " p.title, p.description, p.created_on, p.deadline, p.project_status " +
            " FROM project_user pu " +
            " INNER JOIN projects p ON pu.project_id = p.project_id" +
            " WHERE pu.user_id = :userId;", nativeQuery = true)
    Set<ProjectUser> findAllUsersProjects(long userId) throws ProjectNotFoundException;

    @Query(value = "SELECT pu.project_id, pu.user_id, pu.permissions," +
            " p.title, p.description, p.created_on, p.deadline, p.project_status " +
            " FROM project_user pu " +
            " INNER JOIN projects p ON pu.project_id = p.project_id" +
            " WHERE pu.user_id = :userId AND pu.permissions = :permission;", nativeQuery = true)
    Set<ProjectUser> findAllUsersProjects(long userId, int permission) throws ProjectNotFoundException;
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM project_user pu " +
            " WHERE pu.project_id = :projectId;", nativeQuery = true)
    void deleteAllByProjectId(long projectId) throws ProjectNotFoundException;

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM project_user pu " +
            " WHERE pu.user_id = :userId;", nativeQuery = true)
    void deleteAllByUserId(long userId) throws UserNotFoundException;

}
