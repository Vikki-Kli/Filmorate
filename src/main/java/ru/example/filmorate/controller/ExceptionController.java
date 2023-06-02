package ru.example.filmorate.controller;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.example.filmorate.exception.NoSuchFilmException;
import ru.example.filmorate.exception.NoSuchUserException;
import ru.example.filmorate.exception.ValidationFilmException;

import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ValidationFilmException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationFilmExceptionHandler(Exception e) {
        return Map.of("error: ", e.getMessage());
    }

    @ExceptionHandler({NoSuchUserException.class, NoSuchFilmException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> objectNotFoundHandler(Exception e) {
        return Map.of("error: ", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> someExceptionHandler(Exception e) {
        return Map.of("error: ", e.getMessage());
    }
}
