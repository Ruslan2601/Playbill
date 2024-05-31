package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.user.NewUserRequest;
import ru.practicum.main.service.dto.user.UserResponse;
import ru.practicum.main.service.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminUserController {

    private final UserService service;

    @GetMapping
    public List<UserResponse> getUsers(@RequestParam(required = false) List<Long> ids,
                                       @PositiveOrZero(message = "Поле from отрицательное") @RequestParam(defaultValue = "0") int from,
                                       @Positive(message = "Поле size не положительное") @RequestParam(defaultValue = "10") int size) {
        log.info("=== GET Запрос - getUsers. ids: {}, from: {}, size: {} ===", ids, from, size);
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("=== POST Запрос - addUser. newUserRequest: {} ===", newUserRequest);
        return service.addUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId) {
        log.info("=== DELETE Запрос - deleteUser. userId: {} ===", userId);
        service.deleteUser(userId);
    }
}
