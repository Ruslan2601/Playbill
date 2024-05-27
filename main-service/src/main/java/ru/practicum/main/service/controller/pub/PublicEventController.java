package ru.practicum.main.service.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.event.EventFullResponse;
import ru.practicum.main.service.dto.event.EventShortResponse;
import ru.practicum.main.service.enums.StateSortEvent;
import ru.practicum.main.service.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {

    private final EventService service;

    @GetMapping
    public List<EventShortResponse> searchEvents(@RequestParam(required = false) String text,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                 @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                 @RequestParam(required = false) StateSortEvent sort,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        log.info("=== GET Запрос - getEvents. text: {}, categories: {}, paid: {}, rangeStart: {}, " +
                        "rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}, HttpServletRequest {} ===",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
        return service.searchEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullResponse getEvent(@Positive(message = "Поле id не положительное или null") @PathVariable(name = "id") long eventId,
                                      HttpServletRequest request) {
        log.info("=== GET Запрос - getEvent. eventId: {}, HttpServletRequest {} ===", eventId, request);
        return service.getEventPublic(eventId, request);
    }
}
