package com.batherphilippa.pin_it_app_be.exceptions;

import com.batherphilippa.pin_it_app_be.exceptions.error.ErrorType;
import com.batherphilippa.pin_it_app_be.exceptions.error.Response;
import com.batherphilippa.pin_it_app_be.exceptions.error.ValidationErrorModel;
import com.batherphilippa.pin_it_app_be.exceptions.error.ValidationErrorResponseModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 422 error
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ValidationErrorResponseModel handleException(ConstraintViolationException cve) {
        logger.info("GlobalExceptionHandle: Custom exception: Validation Error: Entity Not Processable");
        List<ValidationErrorModel> validationErrors = processValidationErrors(cve);
        return ValidationErrorResponseModel
                .builder()
                .errorsList(validationErrors)
                .type(ErrorType.VALIDATION_UNPROCESSABLE_ENTITY_EXCEPTION.getHttpStatus())
                .build();

    }

    private List<ValidationErrorModel> processValidationErrors(ConstraintViolationException cve) {
       List<ValidationErrorModel> validationErrors = new ArrayList<>();
        for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
           ValidationErrorModel validationErrorModel = ValidationErrorModel
                    .builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .constraint(ErrorType.VALIDATION_UNPROCESSABLE_ENTITY_EXCEPTION.getCode())
                    .mapping(violation.getPropertyPath().toString())
                    .detail(violation.getMessage())
                    .build();
           validationErrors.add(validationErrorModel);
        }
        logger.info("GlobalExceptionHandle: processValidationErrors");
        return validationErrors;
    }

    // 401 error
    @ExceptionHandler(value = UnauthorisedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response handleException(UnauthorisedException uae){
        logger.info("GlobalExceptionHandle: Custom exception: UNAUTHORISED_EXCEPTION");
        return new Response(ErrorType.UNAUTHORISED_EXCEPTION.getTimestamp(), ErrorType.UNAUTHORISED_EXCEPTION.getCode(), ErrorType.UNAUTHORISED_EXCEPTION.getHttpStatus(), uae.getMessage());
    }

    // 401 error
    @ExceptionHandler(value = UserNotAuthorisedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response handleException(UserNotAuthorisedException unae){
        logger.info("GlobalExceptionHandle: Custom exception: UNAUTHORISED_EXCEPTION");
        return new Response(ErrorType.USER_UNAUTHORISED_EXCEPTION.getTimestamp(), ErrorType.USER_UNAUTHORISED_EXCEPTION.getCode(), ErrorType.USER_UNAUTHORISED_EXCEPTION.getHttpStatus(), unae.getMessage());
    }


    // 404 error
    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleException(UserNotFoundException unfe){
        logger.info("GlobalExceptionHandle: Custom exception: USER_NOT_FOUND");
        return new Response(ErrorType.USER_NOT_FOUND_EXCEPTION.getTimestamp(), ErrorType.USER_NOT_FOUND_EXCEPTION.getCode(), ErrorType.USER_NOT_FOUND_EXCEPTION.getHttpStatus(), unfe.getMessage());
    }

    // 404 error
    @ExceptionHandler(value = ProjectNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleException(ProjectNotFoundException pnfe){
        logger.info("GlobalExceptionHandle: Custom exception: PROJECT_NOT_FOUND");
        return new Response(ErrorType.PROJECT_NOT_FOUND_EXCEPTION.getTimestamp(), ErrorType.PROJECT_NOT_FOUND_EXCEPTION.getCode(), ErrorType.PROJECT_NOT_FOUND_EXCEPTION.getHttpStatus(), pnfe.getMessage());
    }

    // 404 error
    @ExceptionHandler(value = TaskNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleException(TaskNotFoundException tnfe) {
        logger.info("GlobalExceptionHandle: Custom exception: TASK_NOT_FOUND");
        return new Response(ErrorType.TASK_NOT_FOUND_EXCEPTION.getTimestamp(), ErrorType.TASK_NOT_FOUND_EXCEPTION.getCode(), ErrorType.TASK_NOT_FOUND_EXCEPTION.getHttpStatus(), tnfe.getMessage());
    }

    // 404 error
    @ExceptionHandler(value = GuestNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleException(GuestNotFoundException gnfe) {
        logger.info("GlobalExceptionHandle: Custom exception: GUEST_NOT_FOUND");
        return new Response(ErrorType.GUEST_NOT_FOUND_EXCEPTION.getTimestamp(), ErrorType.GUEST_NOT_FOUND_EXCEPTION.getCode(), ErrorType.GUEST_NOT_FOUND_EXCEPTION.getHttpStatus(), gnfe.getMessage());
    }

    // 409 error
    @ExceptionHandler(value = UserExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response handleException(UserExistsException uee){
        logger.info("GlobalExceptionHandle: Custom exception: USER_EXISTS");
        return new Response(ErrorType.USER_EXISTS_EXCEPTION.getTimestamp(), ErrorType.USER_EXISTS_EXCEPTION.getCode(), ErrorType.USER_EXISTS_EXCEPTION.getHttpStatus(), uee.getMessage());
    }

    // 406 error
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public String handleHttpMediaTypeNotAcceptableException() {
        return "unacceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE;
    }

    // 409 error
    @ExceptionHandler(value = GuestInvitationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response handleException(GuestInvitationException gie) {
        logger.info("GlobalExceptionHandle: Custom exception: GUEST_EXISTS");
        return new Response(ErrorType.GUEST_PROJECT_EXISTS_EXCEPTION.getTimestamp(), ErrorType.GUEST_PROJECT_EXISTS_EXCEPTION.getCode(), ErrorType.GUEST_PROJECT_EXISTS_EXCEPTION.getHttpStatus(), gie.getMessage());
    }


}
