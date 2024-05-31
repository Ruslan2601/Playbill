package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EventRequest;
import ru.practicum.stats.dto.StatisticResponse;
import ru.practicum.stats.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;

    @GetMapping("/stats")
    public List<StatisticResponse> getStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                 @RequestParam(required = false) List<String> uris,
                                                 @RequestParam(defaultValue = "false") boolean unique) {
        log.info("=== GET Запрос - getStatistics. start: {}, end: {}, uris: {}, unique: {} ===", start, end, uris, unique);
        return service.getStatistics(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addEvent(@RequestBody EventRequest eventRequest) {
        log.info("=== POST Запрос - addEvent. EventRequest: {}", eventRequest);
        service.addEvent(eventRequest);
    }
}
