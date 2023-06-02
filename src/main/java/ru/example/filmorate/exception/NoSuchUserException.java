package ru.example.filmorate.exception;

public class NoSuchUserException extends Exception {
    public NoSuchUserException(String s) {
        super(s);
    }
}
