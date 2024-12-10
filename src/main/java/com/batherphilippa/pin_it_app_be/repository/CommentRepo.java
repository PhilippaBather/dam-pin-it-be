package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.model.TaskComment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CommentRepo - the Comment Repository containing additional helper methods.
 */
@Repository
public interface CommentRepo extends CrudRepository<TaskComment, Long> {

    List<TaskComment> findByTaskId(long taskId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comments as c " +
            "WHERE c.task_id = :taskId;", nativeQuery = true)
    void deleteByTaskId(long taskId);
}
