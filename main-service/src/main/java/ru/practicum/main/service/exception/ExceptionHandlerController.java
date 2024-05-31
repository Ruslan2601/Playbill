package ru.practicum.main.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.service.exception.model.ConflictException;
import ru.practicum.main.service.exception.model.NotExistException;
import ru.practicum.main.service.exception.response.ApiError;
import ru.practicum.stats.exception.ServerResponseException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice("ru.practicum.main.service")
@Slf4j
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError valid(MethodArgumentNotValidException exception) {
        List<String> exceptionReasons = new ArrayList<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            exceptionReasons.add(fieldError.getDefaultMessage());
        }

        ApiError response = new ApiError(
                Arrays.toString(exception.getStackTrace()),
                exceptionReasons.toString(),
                "Ошибка при валидации объекта",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());

        log.warn(response.toString());

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError validated(ConstraintViolationException exception) {
        ApiError response = new ApiError(
                Arrays.toString(exception.getStackTrace()),
                exception.getMessage(),
                "Ошибка при валидации объекта",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());

        log.warn(response.toString());

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError invalidRequest(HttpMessageNotReadableException exception) {
        ApiError response = new ApiError(
                Arrays.toString(exception.getStackTrace()),
                exception.getMessage(),
                "Запрос составлен неправильно",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());

        log.warn(response.toString());

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError invalidRequest(MissingServletRequestParameterException exception) {
        ApiError response = new ApiError(
                Arrays.toString(exception.getStackTrace()),
                exception.getMessage(),
                "Запрос составлен неправильно",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());

        log.warn(response.toString());

        return response;
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiError jdbcException(JDBCException exception) {
        ApiError response = new ApiError(
                Arrays.toString(exception.getStackTrace()),
                exception.getSQLException().getMessage(),
                "Ошибка при работе с БД",
                HttpStatus.CONFLICT,
                LocalDateTime.now());

        log.warn(response.toString());

        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ApiError notFount(NotExistException exception) {
        ApiError response = new ApiError(
                Arrays.toString(exception.getStackTrace()),
                exception.getMessage(),
                "Обращение к несуществующему обьекту",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());

        log.warn(response.toString());

        return response;

    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiError conflict(ConflictException exception) {
        ApiError response = new ApiError(
                Arrays.toString(exception.getStackTrace()),
                exception.getMessage(),
                "Конфликт параметров",
                HttpStatus.CONFLICT,
                LocalDateTime.now());

        log.warn(response.toString());

        return response;

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError statsServerProblem(ServerResponseException exception) {
        ApiError response = new ApiError(
                Arrays.toString(exception.getStackTrace()),
                exception.getMessage(),
                "Сервер статистики не смог обработать информацию",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());

        log.warn(response.toString());

        return response;

    }
}
