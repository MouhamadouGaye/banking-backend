package com.mgaye.banking_backend.exception;

import org.hibernate.TransactionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mgaye.banking_backend.dto.error.ApiError;

import java.util.Map;
import java.util.stream.Collectors;

// exception/GlobalExceptionHandler.java
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
                ApiError apiError = new ApiError(
                                "RESOURCE_NOT_FOUND",
                                ex.getMessage(),
                                ex.getResourceName(),
                                Map.of(
                                                "field", ex.getFieldName(),
                                                "value", ex.getFieldValue()));
                return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex) {
                ApiError apiError = new ApiError(
                                "AUTHENTICATION_FAILED",
                                "Invalid username or password",
                                "AUTHENTICATION");
                return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
                ApiError apiError = new ApiError(
                                "ACCESS_DENIED",
                                ex.getMessage(),
                                "AUTHORIZATION");
                return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
        }

        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex,
                        HttpHeaders headers,
                        HttpStatus status,
                        WebRequest request) {

                Map<String, Object> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .collect(Collectors.toMap(
                                                FieldError::getField,
                                                fieldError -> fieldError.getDefaultMessage() == null ? "Invalid value"
                                                                : fieldError.getDefaultMessage()));

                ApiError error = new ApiError(
                                "VALIDATION_FAILED",
                                "Input validation failed",
                                "VALIDATION",
                                errors);
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGlobalException(Exception ex) {
                ApiError apiError = new ApiError(
                                "INTERNAL_ERROR",
                                "An unexpected error occurred",
                                "SYSTEM");
                return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}