package ru.practicum.main.service.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.request.ParticipationRequestResponse;
import ru.practicum.main.service.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateRequestController {

    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestResponse> getRequests(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId) {
        log.info("=== GET Запрос - getRequests. userId: {} ===", userId);
        return service.getRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestResponse addRequest(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId,
                                                   @Positive(message = "Поле eventId не положительное или null") @RequestParam long eventId) {
        log.info("=== POST Запрос - addRequest. userId: {}, eventId: {} ===", userId, eventId);
        return service.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestResponse cancelRequest(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId,
                                                      @Positive(message = "Поле requestId не положительное или null") @PathVariable long requestId) {
        log.info("=== GET Запрос - getRequests. userId: {}, requestId: {} ===", userId, requestId);
        return service.cancelRequest(userId, requestId);
    }
}
