package ru.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.service.dto.category.CategoryResponse;
import ru.practicum.main.service.dto.category.NewCategoryRequest;
import ru.practicum.main.service.model.Category;

@Component
public class CategoryMapper {

    public Category toCategory(NewCategoryRequest newCategoryRequest) {
        Category category = new Category();
        category.setName(newCategoryRequest.getName());
        return category;
    }

    public CategoryResponse toCategoryDto(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
