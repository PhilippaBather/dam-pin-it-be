package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.controller.TaskController;
import com.batherphilippa.pin_it_app_be.dto.TaskDTOIn;
import com.batherphilippa.pin_it_app_be.dto.TaskUpdatedDTOIn;
import com.batherphilippa.pin_it_app_be.exceptions.TaskNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Task;
import com.batherphilippa.pin_it_app_be.repository.TaskRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * TaskService - the implementation of the Task Service interface (ITaskService).
 */
@Service
public class TaskService implements ITaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final ModelMapper modelMapper;
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo, ModelMapper modelMapper) {
        this.taskRepo = taskRepo;
        this.modelMapper = modelMapper;
    }
    @Override
    public Set<Task> getAllTaskDetailsByProjectId(long projectId) {
        Set<Task> taskSet = taskRepo.findAllTasksByProjectId(projectId);
        System.out.println(taskSet.size());
        return taskSet;
    }

    @Override
    public Task getTaskById(long taskId) throws TaskNotFoundException {
        return taskRepo.findTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Override
    public Task saveTask(TaskDTOIn taskDTOIn) {
        Task task = new Task();
        modelMapper.map(taskDTOIn, task);
        task.setId(null);
        logger.info("TaskController: save task");
        return taskRepo.save(task);
    }

    @Override
    public void saveTasks(Set<TaskUpdatedDTOIn> taskSet) {
        taskSet.forEach((task) -> {
            System.out.println(task.getTaskStatus());
            taskRepo.updateTaskById(task.getId(), task.getDeadline(), task.getDescription(), task.getPriorityLevel(), task.getTitle(), task.getTaskPosition(), task.getTaskStatus());
        });
    }

    @Override
    public Task updateTaskById(long taskId, TaskDTOIn taskDTOIn) throws TaskNotFoundException {
        taskRepo.findTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        taskRepo.updateTaskById(taskId, taskDTOIn.getDeadline(), taskDTOIn.getDescription(), taskDTOIn.getPriorityLevel(),
                taskDTOIn.getTitle(), taskDTOIn.getTaskPosition(), taskDTOIn.getTaskStatus());
        return taskRepo.findTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Override
    public void deleteTaskById(long taskId) {
        Task task = taskRepo.findTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        taskRepo.delete(task);
    }

    @Override
    public void deleteAllTasksByProjectId(long projectId) {
        taskRepo.deleteAllByProjectId(projectId);
    }

}
