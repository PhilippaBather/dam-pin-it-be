package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.TaskDTOIn;
import com.batherphilippa.pin_it_app_be.dto.TaskDetailsDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.ProjectNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.TaskNotFoundException;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Task;
import com.batherphilippa.pin_it_app_be.service.ProjectService;
import com.batherphilippa.pin_it_app_be.service.TaskService;
import com.batherphilippa.pin_it_app_be.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final ProjectService projectService;
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(ProjectService projectService, TaskService taskService, UserService userService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<Set<TaskDetailsDTOOut>> getAllTasksByProjectId(@PathVariable long userId, @PathVariable long projectId) throws ProjectNotFoundException, UserNotFoundException {
        // check project and user exist or throw corresponding exception
        projectService.getProjectById(projectId, userId);
        userService.findById(userId);
        // get tasks
        Set<TaskDetailsDTOOut> taskDetailsDTOOut = taskService.getAllTaskDetailsByProjectId(projectId);
        return new ResponseEntity<>(taskDetailsDTOOut, HttpStatus.OK);

    }
    @PostMapping("/tasks")
    public ResponseEntity<Task> saveTask(@PathVariable long userId, @PathVariable long projectId, @Valid @RequestBody TaskDTOIn taskDTOIn) throws ProjectNotFoundException, UserNotFoundException {
        // check project and user exist or throw corresponding exception
        projectService.getProjectById(projectId, userId);
        userService.findById(userId);
        // save task
        Task task = taskService.saveTask(taskDTOIn);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/user/{userId}/project/{projectId}")
    public ResponseEntity<Void> deleteAllTasksByProjectId(@PathVariable long userId, @PathVariable long projectId) throws ProjectNotFoundException {
        // check project and user exist or throw corresponding exception
        projectService.getProjectById(projectId, userId);
        userService.findById(userId);
        taskService.deleteAllTasksByProjectId(projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}/user/{userId}/project/{projectId}")
    public ResponseEntity<Task> getTaskById(@PathVariable long taskId, @PathVariable long projectId, @PathVariable long userId) throws ProjectNotFoundException, UserNotFoundException, TaskNotFoundException {
        // check project and user exist or throw corresponding exception
        projectService.getProjectById(projectId, userId);
        userService.findById(userId);
        Task task = taskService.getTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/task/{taskId}/user/{userId}/project/{projectId}")
    public ResponseEntity<Task> updateTaskById(@PathVariable long projectId, @PathVariable long userId, @PathVariable long taskId, @Valid @RequestBody TaskDTOIn taskDTOIn) throws ProjectNotFoundException, UserNotFoundException, TaskNotFoundException {
        // check project and user exist or throw corresponding exception
        projectService.getProjectById(projectId, userId);
        userService.findById(userId);
        Task task = taskService.updateTaskById(taskId, taskDTOIn);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/task/{taskId}/user/{userId}/project/{projectId}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable long projectId, @PathVariable long userId, @PathVariable long taskId) throws ProjectNotFoundException, UserNotFoundException, TaskNotFoundException {
        // check project and user exist or throw corresponding exception
        projectService.getProjectById(projectId, userId);
        userService.findById(userId);
        taskService.deleteTaskById(taskId);
        return ResponseEntity.noContent().build();
    }
}
