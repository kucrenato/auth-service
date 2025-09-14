package com.bci.auth_service.errors;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
        MethodArgumentNotValidException ex) {

        List<ApiError> errors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ((FieldError) error).getField() + ": " + error.getDefaultMessage()
            ))
            .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex) {

        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(new ApiErrorResponse(Collections.singletonList(error)),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(
        EmailAlreadyExistsException ex) {

        ApiError error = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(new ApiErrorResponse(Collections.singletonList(error)),
            HttpStatus.CONFLICT);
    }

}
