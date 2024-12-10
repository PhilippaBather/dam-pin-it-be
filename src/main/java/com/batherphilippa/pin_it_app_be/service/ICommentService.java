package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.CommentDTOIn;
import com.batherphilippa.pin_it_app_be.dto.CommentDTOOut;
import com.batherphilippa.pin_it_app_be.exceptions.TaskNotFoundException;

import java.util.List;

/**
 * ICommentService - Comment Service interface defining the methods to be used.
 */
public interface ICommentService {

    List<CommentDTOOut> getComments(long taskId) throws TaskNotFoundException;
    CommentDTOOut saveComment(long taskId, long userId, CommentDTOIn commentDTOIn) throws TaskNotFoundException;
}
