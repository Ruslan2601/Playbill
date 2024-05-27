package ru.practicum.stats.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice("ru.practicum.stats.server")
@Slf4j
public class ExceptionHandlerController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse invalidRequest(HttpMessageNotReadableException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Запрос составлен неправильно",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ExceptionHandlerResponse jdbcException(JDBCException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Ошибка при работе с БД",
                exception.getSQLException().getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionHandlerResponse validated(ConstraintViolationException exception) {
        ExceptionHandlerResponse response = new ExceptionHandlerResponse("Ошибка при валидации объекта",
                exception.getMessage());

        log.warn(response.toString());

        return response;
    }
}
