package ru.practicum.exception.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import ru.practicum.exception.dto.ApiError;
import ru.practicum.exception.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        return new ApiError(
                List.of(sw.toString()),
                "An error occurred while processing request",
                "Exception",
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors =
                e.getConstraintViolations().stream()
                        .map(GlobalExceptionHandler::toViolationString)
                        .collect(Collectors.toList());

        return new ApiError(
                errors,
                e.getMessage(),
                "Validation failed",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors =
                e.getBindingResult().getFieldErrors().stream()
                        .map(GlobalExceptionHandler::toFieldErrorString)
                        .collect(Collectors.toList());

        return new ApiError(
                errors,
                e.getMessage(),
                "Validation failed",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler({ValidationException.class, IllegalEventUpdateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(RuntimeException e) {
        return new ApiError(
                null,
                e.getMessage(),
                "Bad request",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException e) {
        return new ApiError(
                null,
                e.getMessage(),
                "Not found",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(Exception e) {
        return new ApiError(
                null,
                e.getMessage(),
                "Data integrity violation",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbidden(ForbiddenAccessException e) {
        return new ApiError(
                null,
                e.getMessage(),
                "Forbidden",
                HttpStatus.FORBIDDEN.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRequestParamExceptions(Exception e) {
        return new ApiError(
                null,
                e.getMessage(),
                "Validation failed",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    private static String toViolationString(ConstraintViolation<?> violation) {
        return violation.getPropertyPath() + ": " + violation.getMessage();
    }

    private static String toFieldErrorString(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
