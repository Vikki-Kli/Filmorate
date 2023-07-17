package ru.example.filmorate.exception;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String s) {
        super(s);
    }
}
