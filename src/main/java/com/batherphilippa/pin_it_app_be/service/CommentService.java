package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.CommentDTOIn;
import com.batherphilippa.pin_it_app_be.dto.CommentDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.TaskNotFoundException;
import com.batherphilippa.pin_it_app_be.model.Task;
import com.batherphilippa.pin_it_app_be.model.TaskComment;
import com.batherphilippa.pin_it_app_be.model.User;
import com.batherphilippa.pin_it_app_be.repository.CommentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * CommentService - the implementation of the Comment Service interface (ICommentService).
 */
@Service
public class CommentService implements ICommentService {

    private final ModelMapper modelMapper;
    private final CommentRepo commentRepo;
    private final TaskService taskService;
    private final UserService userService;

    public CommentService(CommentRepo commentRepo, TaskService taskService, UserService userService, ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.taskService = taskService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CommentDTOOut> getComments(long taskId) throws TaskNotFoundException {
        Task task = taskService.getTaskById(taskId);
        List<TaskComment> taskCommentList = commentRepo.findByTaskId(taskId);
        // convert to task to DTO out
        return convertToDTOOut(taskCommentList);
    }

    @Override
    public CommentDTOOut saveComment(long taskId, long userId, CommentDTOIn commentDTOIn) throws TaskNotFoundException {
        User user = userService.findById(userId);
        Task task = taskService.getTaskById(taskId);

        TaskComment savedComment = mapAndSaveData(commentDTOIn, user, task);

        return mapToDTOOut(savedComment, user);
    }

    private TaskComment mapAndSaveData(CommentDTOIn commentDTOIn, User user, Task task) {
        TaskComment comment = new TaskComment();
        modelMapper.map(commentDTOIn, comment);
        comment.setUser(user);
        comment.setTask(task);
          return commentRepo.save(comment);
    }

    private CommentDTOOut mapToDTOOut(TaskComment savedComment, User user) {
        CommentDTOOut commentDTOOut = new CommentDTOOut();
        modelMapper.map(savedComment, commentDTOOut);
        String auth = user.getForename().concat(" ").concat(user.getSurname());
        commentDTOOut.setAuthor(auth);
        return commentDTOOut;
    }

    private List<CommentDTOOut> convertToDTOOut(List<TaskComment> taskCommentSet) {
        List<CommentDTOOut> commentDTOOutSet = new ArrayList<>();
        for(int i = 0; i < taskCommentSet.size(); i++) {
            CommentDTOOut commentDTOOut = new CommentDTOOut();
            modelMapper.map(taskCommentSet.get(i), commentDTOOut);
            String auth = taskCommentSet.get(i).getUser().getForename().concat(" ").concat(taskCommentSet.get(i).getUser().getSurname());
            commentDTOOut.setAuthor(auth);
            commentDTOOutSet.add(commentDTOOut);
        }
        return commentDTOOutSet;
    }
}
