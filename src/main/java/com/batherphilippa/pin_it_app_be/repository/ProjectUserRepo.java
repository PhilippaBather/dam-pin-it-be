package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.model.ProjectUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectUserRepo extends CrudRepository<ProjectUser, Long> {

    @Query(value = "SELECT pu.project_id, pu.user_id, pu.permissions," +
            " p.title, p.description, p.created_on, p.deadline, p.project_status " +
            " FROM project_user pu " +
            " INNER JOIN projects p ON pu.project_id = p.project_id" +
            " WHERE pu.user_id = :userId;", nativeQuery = true)
    Set<ProjectUser> findAllUsersProjects(long userId) throws ProjectNotFoundException;

    ProjectUser findProjectUserByProjectIdAndUserId(long projectId, long userId);

}
