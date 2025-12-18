package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class StatExceptionHandler {

    @ResponseStatus
    @ExceptionHandler(IllegalArgumentException.class)
    public StatError handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Получено исключение: {}", e.getMessage(), e);
        return new StatError(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }
}
