package ru.practicum.main.categories.admin.service;

import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.NewCategoryDto;
import ru.practicum.main.categories.model.Category;

public interface AdminCategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(int catId);

    CategoryDto changeCategory(int catId, CategoryDto categoryDto);

    Category findCategoryById(int catId);
}
