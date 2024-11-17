package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.TaskDTOIn;
import com.batherphilippa.pin_it_app_be.dto.TaskDetailsDTOOut;
import com.batherphilippa.pin_it_app_be.model.Task;

import java.util.Set;

/**
 * ITaskService - Task Service interface defining the methods to be used.
 */
public interface ITaskService {

    Set<Task> getAllTaskDetailsByProjectId(long projectId);
    Task getTaskById(long taskId);
    Task saveTask(TaskDTOIn taskDTOIn);
    Task updateTaskById(long taskId, TaskDTOIn taskDTOIn);
    void deleteTaskById(long taskId);
    void deleteAllTasksByProjectId(long projectId);
}
