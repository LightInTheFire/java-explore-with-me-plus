package ru.practicum.exception.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.dto.ApiError;
import ru.practicum.exception.dto.Violation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        log.error(e.getMessage(), e);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        return new ApiError(
                List.of(stackTrace),
                "An error occured while processing request",
                "Exception",
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations =
                e.getConstraintViolations().stream()
                        .map(
                                violation ->
                                        new Violation(
                                                violation.getPropertyPath().toString(),
                                                violation.getMessage()))
                        .collect(Collectors.toList());
        log.warn(violations.toString());
        List<String> errors = violations.stream().map(Violation::toString).toList();
        return new ApiError(
                errors,
                e.getMessage(),
                "Some fields of RequestBody for request are invalid",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations =
                e.getBindingResult().getFieldErrors().stream()
                        .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.toList());
        log.warn(violations.toString());
        List<String> errors = violations.stream().map(Violation::toString).toList();
        return new ApiError(
                errors,
                e.getMessage(),
                "Some fields of RequestBody for request are invalid",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFoundException(NotFoundException e) {
        log.warn("Not found: {}", e.getMessage());
        return new ApiError(
                List.of(e.getMessage()),
                e.getMessage(),
                "The required object was not found",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn(e.getMessage(), e);
        return new ApiError(
                List.of(e.getMessage()),
                e.getMessage(),
                "Some fields of RequestBody for request are invalid",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }
}
