package ru.practicum.main.service.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.event.*;
import ru.practicum.main.service.dto.request.ParticipationRequestResponse;
import ru.practicum.main.service.service.EventService;
import ru.practicum.main.service.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateEventController {

    private final EventService service;
    private final RequestService requestService;

    @GetMapping
    public List<EventShortResponse> getEvents(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("=== GET Запрос - getEvents. userId: {}, from: {}, size: {} ===", userId, from, size);
        return service.getEventsAdmin(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullResponse getEvent(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId,
                                      @Positive(message = "Поле eventId не положительное или null") @PathVariable long eventId) {
        log.info("=== GET Запрос - getEvent. userId: {}, eventId: {} ===", userId, eventId);
        return service.getEventAdmin(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestResponse> getRequestsOnEvent(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId,
                                                                 @Positive(message = "Поле eventId не положительное или null") @PathVariable long eventId) {
        log.info("=== GET Запрос - getRequestsOnEvent. userId: {}, eventId: {} ===", userId, eventId);
        return requestService.getRequestsOnEvent(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullResponse addEvent(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId,
                                      @Valid @RequestBody NewEventRequest newEventRequest) {
        log.info("=== POST Запрос - addEvent. userId: {}, newEventRequest: {} ===", userId, newEventRequest);
        return service.addEventUser(userId, newEventRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullResponse updateEvent(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId,
                                         @Positive(message = "Поле eventId не положительное или null") @PathVariable long eventId,
                                         @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("=== PATCH Запрос - updateEvent. userId: {}, eventId: {} ===", userId, eventId);
        return service.updateEventUser(userId, eventId, updateEventUserRequest);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResponse updateRequestStatusOnEvent(@Positive(message = "Поле userId не положительное или null") @PathVariable long userId,
                                                                       @Positive(message = "Поле eventId не положительное или null") @PathVariable long eventId,
                                                                       @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("=== PATCH Запрос - updateEvent. userId: {}, eventId: {}, eventRequestStatusUpdateRequest: {} ===", userId, eventId, eventRequestStatusUpdateRequest);
        return requestService.updateRequestStatusOnEvent(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
