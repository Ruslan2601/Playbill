package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.event.EventFullResponse;
import ru.practicum.main.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.main.service.enums.StateEvent;
import ru.practicum.main.service.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {

    private final EventService service;

    @GetMapping
    public List<EventFullResponse> searchEvents(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<StateEvent> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("=== GET Запрос - searchEvents. users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, from: {}, size: {} ===",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return service.searchEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullResponse updateEvent(@Positive(message = "Поле eventId не положительное или null") @PathVariable long eventId,
                                         @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("=== PATCH Запрос - updateEvent. eventId: {}, updateEventAdminRequest: {} ===", eventId, updateEventAdminRequest);
        return service.updateEventAdmin(eventId, updateEventAdminRequest);
    }
}
