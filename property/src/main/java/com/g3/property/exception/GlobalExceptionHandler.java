package com.g3.property.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.g3.property.dto.ErrorResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({AgentNotFoundException.class, ListingNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException (RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        logger.error("ResourceNotFound exception occurred: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AgentAlreadyExistsException.class })
    public ResponseEntity<ErrorResponse> handleAgentAlreadyExistsException (RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        logger.error("AgentAlreadyExistsException exception occurred: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> validationErrors = ex.getBindingResult().getAllErrors();

        StringBuilder sb = new StringBuilder();

        for (ObjectError error : validationErrors) {
            sb.append(error.getDefaultMessage() + ".");
        }

        ErrorResponse errorResponse = new ErrorResponse(sb.toString(), LocalDateTime.now());
        logger.error("MethodArgumentNotValid exception occurred: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Unauthorized to edit or delete Listing of other agents
    @ExceptionHandler({UnauthorizedAccessException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException (RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        logger.error(" UnauthorizedAccess exception occurred: ", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occured. Please report error.", LocalDateTime.now());
        logger.error("Unexpected error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
