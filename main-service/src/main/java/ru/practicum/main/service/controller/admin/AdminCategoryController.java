package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.category.CategoryResponse;
import ru.practicum.main.service.dto.category.NewCategoryRequest;
import ru.practicum.main.service.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCategoryController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@Valid @RequestBody NewCategoryRequest newCategoryRequest) {
        log.info("=== POST Запрос - addCategory. newCategoryRequest: {} ===", newCategoryRequest);
        return service.addCategory(newCategoryRequest);
    }

    @PatchMapping("/{catId}")
    public CategoryResponse updateCategory(@Positive(message = "Поле catId не положительное или null") @PathVariable long catId,
                                           @Valid @RequestBody NewCategoryRequest newCategoryRequest) {
        log.info("=== PATCH Запрос - updateCategory. catId: {}, newCategoryRequest: {} ===", catId, newCategoryRequest);
        return service.updateCategory(catId, newCategoryRequest);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Positive(message = "Поле catId не положительное или null") @PathVariable long catId) {
        log.info("=== DELETE Запрос - deleteCategory. catId: {} ===", catId);
        service.deleteCategory(catId);
    }
}
