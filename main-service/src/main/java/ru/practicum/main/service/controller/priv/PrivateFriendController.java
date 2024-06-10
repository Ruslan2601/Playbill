package ru.practicum.main.service.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.event.EventShortResponse;
import ru.practicum.main.service.dto.user.UserResponse;
import ru.practicum.main.service.service.UserService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/friends")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateFriendController {

    private final UserService service;

    @GetMapping("/{friendId}")
    public List<EventShortResponse> getFriendRequests(@Positive(message = "Поле userId не положительное") @PathVariable long userId,
                                                      @Positive(message = "Поле friendId не положительное") @PathVariable long friendId) {
        log.info("=== GET Запрос - getFriendRequests. userId: {}, friendId: {} ===", userId, friendId);
        return service.getFriendRequests(userId, friendId);
    }

    @GetMapping
    public List<EventShortResponse> getFriendsRequests(@Positive(message = "Поле userId не положительное") @PathVariable long userId) {
        log.info("=== GET Запрос - getFriendsRequests. userId: {} ===", userId);
        return service.getFriendsRequests(userId);
    }

    @PostMapping("/{friendId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addFriend(@Positive(message = "Поле userId не положительное") @PathVariable long userId,
                                  @Positive(message = "Поле friendId не положительное") @PathVariable long friendId) {
        log.info("=== POST Запрос - addFriend. userId: {}, friendId: {} ===", userId, friendId);
        return service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@Positive(message = "Поле userId не положительное") @PathVariable long userId,
                             @Positive(message = "Поле friendId не положительное") @PathVariable long friendId) {
        log.info("=== DELETE Запрос - deleteFriend. userId: {}, friendId: {} ===", userId, friendId);
        service.deleteFriend(userId, friendId);
    }
}
