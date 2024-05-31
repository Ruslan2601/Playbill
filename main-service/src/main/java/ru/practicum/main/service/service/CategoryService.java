package ru.practicum.main.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.category.CategoryResponse;
import ru.practicum.main.service.dto.category.NewCategoryRequest;
import ru.practicum.main.service.exception.model.NotExistException;
import ru.practicum.main.service.mapper.CategoryMapper;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.repository.CategoryRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public List<CategoryResponse> getCategories(int from, int size) {
        return repository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(mapper::toCategoryDto)
                .collect(toList());
    }

    public CategoryResponse getCategory(long catId) {
        return mapper.toCategoryDto(findCategory(catId));
    }

    @Transactional
    public CategoryResponse addCategory(NewCategoryRequest newCategoryRequest) {
        return mapper.toCategoryDto(repository.save(mapper.toCategory(newCategoryRequest)));
    }

    @Transactional
    public CategoryResponse updateCategory(long catId, NewCategoryRequest newCategoryRequest) {
        Category category = findCategory(catId);

        if (category.getName().equals(newCategoryRequest.getName())) {
            return mapper.toCategoryDto(category);
        }

        category.setName(newCategoryRequest.getName());

        return mapper.toCategoryDto(repository.save(category));
    }

    @Transactional
    public void deleteCategory(long catId) {
        findCategory(catId);
        repository.deleteById(catId);
    }

    private Category findCategory(long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotExistException("Category с id = " + catId + " не существует"));
    }
}
