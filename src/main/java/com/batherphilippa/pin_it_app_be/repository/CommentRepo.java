package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.model.TaskComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CommentRepo - the Comment Repository containing additional helper methods.
 */
@Repository
public interface CommentRepo extends CrudRepository<TaskComment, Long> {

    List<TaskComment> findByTaskId(long taskId);
}
