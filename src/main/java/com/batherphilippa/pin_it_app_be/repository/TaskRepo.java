package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.dto.TaskDetailsDTOOut;
import com.batherphilippa.pin_it_app_be.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * TaskRepo - the Task Repository containing additional helper methods.
 */
@Repository
public interface TaskRepo extends CrudRepository<Task, Long> {

    @Query(value = "SELECT t.task_id, t.task_deadline, t.priority_level, t.title FROM tasks t " +
            " WHERE project_id = :projectId;", nativeQuery = true)
    Set<TaskDetailsDTOOut> findAllTasksByProjectId(long projectId);

    Optional<Task> getTaskById(long taskId);

    @Query(value = "DELETE * FROM tasks t " +
            " WHERE project_id =:projectId;", nativeQuery = true)
    void deleteAllByProjectId(long projectId);

}
