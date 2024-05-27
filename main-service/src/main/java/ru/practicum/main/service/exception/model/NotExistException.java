package ru.practicum.main.service.exception.model;

public class NotExistException extends RuntimeException {

    public NotExistException(String message) {
        super(message);
    }
}
