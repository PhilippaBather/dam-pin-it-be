package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.model.Priority;
import com.batherphilippa.pin_it_app_be.model.Task;
import com.batherphilippa.pin_it_app_be.model.TaskStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

/**
 * TaskRepo - the Task Repository containing additional helper methods.
 */
@Repository
public interface TaskRepo extends CrudRepository<Task, Long> {

    @Query(value = "SELECT t.task_id, t.task_deadline, t.task_description, t.priority_level, t.task_title, t.project_id, " +
            " t.task_position, t.task_status FROM tasks t " +
            " WHERE t.project_id = :projectId;", nativeQuery = true)
    Set<Task> findAllTasksByProjectId(long projectId);

    Optional<Task> getTaskById(long taskId);

    @Transactional
    @Modifying(clearAutomatically=true) // clears non-flushed values from the EntityManager
    @Query(value = "UPDATE tasks t" +
            " SET t.task_deadline =:deadline, t.task_description = :description, t.priority_level = :priorityLevel," +
            " t.task_title = :title, t.task_position = :taskPosition, t.task_status = :taskStatus " +
            " WHERE t.task_id = :taskId;", nativeQuery = true)
    void updateTaskById(long taskId, LocalDate deadline, String description, Priority priorityLevel, String title, int taskPosition, TaskStatus taskStatus);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM tasks t " +
            " WHERE t.project_id =:projectId;", nativeQuery = true)
    void deleteAllByProjectId(long projectId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tasks (task_deadline, task_description, priority_level, task_position, task_status, task_title, project_id) " +
            " VALUES(:deadline, :description, :priorityLevel, :taskPosition, :taskStatus, :title, :projectId);", nativeQuery = true)
    void save(LocalDate deadline, String description, Priority priorityLevel, int taskPosition, TaskStatus taskStatus, String title, long projectId);
}
