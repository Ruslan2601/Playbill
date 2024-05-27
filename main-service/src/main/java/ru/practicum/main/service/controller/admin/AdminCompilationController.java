package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.compilation.CompilationResponse;
import ru.practicum.main.service.dto.compilation.NewCompilationRequest;
import ru.practicum.main.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.service.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCompilationController {

    public final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponse addCompilation(@Valid @RequestBody NewCompilationRequest newCompilationRequest) {
        log.info("=== POST Запрос - addCompilation. newCompilationRequest: {} ===", newCompilationRequest);
        return service.addCompilation(newCompilationRequest);
    }

    @PatchMapping("/{compId}")
    public CompilationResponse updateCompilation(@Positive(message = "Поле compId не положительное или null") @PathVariable long compId,
                                                 @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("=== PATCH Запрос - updateCompilation. compId: {},  updateCompilationRequest: {} ===", compId, updateCompilationRequest);
        return service.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive(message = "Поле compId не положительное или null") @PathVariable long compId) {
        log.info("=== DELETE Запрос - deleteCompilation. compId: {} ===", compId);
        service.deleteCompilation(compId);
    }
}
