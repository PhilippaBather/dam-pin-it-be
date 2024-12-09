package com.batherphilippa.pin_it_app_be.controller;

import com.batherphilippa.pin_it_app_be.dto.CommentDTOIn;
import com.batherphilippa.pin_it_app_be.dto.CommentDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.UserNotFoundException;
import com.batherphilippa.pin_it_app_be.service.CommentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final CommentService commentService;
    private CommentController(CommentService commentService) {
        this.commentService = commentService;

    }
    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDTOOut>> getTaskComments(@PathVariable long taskId) {
        List<CommentDTOOut> commentDTOOutSet = commentService.getComments(taskId);
        return new ResponseEntity<>(commentDTOOutSet, HttpStatus.OK);
    }
    @PostMapping("/comments/{taskId}/user/{userId}")
    public ResponseEntity<CommentDTOOut> saveTaskComment(@PathVariable long taskId, @PathVariable long userId, @Valid @RequestBody CommentDTOIn commentDTOIn) throws UserNotFoundException {
        CommentDTOOut commentDTOOut = commentService.saveComment(taskId, userId, commentDTOIn);
        return new ResponseEntity<>(commentDTOOut, HttpStatus.CREATED);
    }
}
