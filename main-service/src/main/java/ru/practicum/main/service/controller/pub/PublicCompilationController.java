package ru.practicum.main.service.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.compilation.CompilationResponse;
import ru.practicum.main.service.service.CompilationService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCompilationController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationResponse> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("=== GET Запрос - getCompilations. pinned: {}, from: {}, size: {} ===", pinned, from, size);
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationResponse getCompilation(@Positive(message = "Поле compId не положительное или null") @PathVariable long compId) {
        log.info("=== GET Запрос - getCompilation. compId: {} ===", compId);
        return service.getCompilation(compId);
    }
}
