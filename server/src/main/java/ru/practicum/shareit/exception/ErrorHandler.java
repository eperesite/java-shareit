package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.warn("Error", e.getMessage());
        return new ErrorResponse(e.getMessage(), "");
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ErrorResponse handleConflict(final ValidatetionConflict e) {
        log.warn("Error", e.getMessage());
        return new ErrorResponse(e.getMessage(), "");
    }
}