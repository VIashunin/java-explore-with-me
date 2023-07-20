package ru.practicum.main.categories.common.service;

import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.model.Category;

import java.util.List;

public interface CommonCategoryService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(int catId);

    Category findCategoryById(int catId);
}
