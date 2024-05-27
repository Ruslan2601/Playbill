package ru.practicum.main.service.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.category.CategoryResponse;
import ru.practicum.main.service.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCategoryController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryResponse> getCategories(@PositiveOrZero(message = "Поле from отрицательное") @RequestParam(defaultValue = "0") int from,
                                                @Positive(message = "Поле size не положительное") @RequestParam(defaultValue = "10") int size) {
        log.info("=== GET Запрос - getCategories. from: {}, size: {} ===", from, size);
        return service.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryResponse getCategory(@Positive(message = "Поле catId не положительное или null") @PathVariable long catId) {
        log.info("=== GET Запрос - getCategory. catId: {} ===", catId);
        return service.getCategory(catId);
    }
}
