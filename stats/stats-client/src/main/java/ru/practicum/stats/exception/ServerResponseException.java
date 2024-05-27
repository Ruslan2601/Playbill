package ru.practicum.stats.exception;

public class ServerResponseException extends RuntimeException {
    public ServerResponseException(String message) {
        super(message);
    }
}
