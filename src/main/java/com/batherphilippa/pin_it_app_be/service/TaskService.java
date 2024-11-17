package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.TaskDTOIn;
import com.batherphilippa.pin_it_app_be.exceptions.TaskNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Task;
import com.batherphilippa.pin_it_app_be.model.TaskStatus;
import com.batherphilippa.pin_it_app_be.repository.TaskRepo;
import org.apache.logging.log4j.spi.LoggerContextFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

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
        return taskRepo.getTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Override
    public void saveTask(TaskDTOIn taskDTOIn) {
        Task task = new Task();
        modelMapper.map(taskDTOIn, task);
        task.setTaskStatus(TaskStatus.setTaskStatusByNum(taskDTOIn.getTaskStatus()));
        logger.info("TaskController: save task");
        taskRepo.save(taskDTOIn.getDeadline(), taskDTOIn.getDescription(), taskDTOIn.getPriorityLevel(), taskDTOIn.getTaskPosition(),
                TaskStatus.setTaskStatusByNum(taskDTOIn.getTaskStatus()), taskDTOIn.getTitle(), taskDTOIn.getProjectId());
    }

    @Override
    public Task updateTaskById(long taskId, TaskDTOIn taskDTOIn) throws TaskNotFoundException {
        taskRepo.getTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        taskRepo.updateTaskById(taskId, taskDTOIn.getDeadline(), taskDTOIn.getDescription(), taskDTOIn.getPriorityLevel(),
                taskDTOIn.getTitle(), taskDTOIn.getTaskPosition(), TaskStatus.setTaskStatusByNum(taskDTOIn.getTaskStatus()));
        return taskRepo.getTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Override
    public void deleteTaskById(long taskId) {
        Task task = taskRepo.getTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        taskRepo.delete(task);
    }


    @Override
    public void deleteAllTasksByProjectId(long projectId) {
        taskRepo.deleteAllByProjectId(projectId);
    }
}
