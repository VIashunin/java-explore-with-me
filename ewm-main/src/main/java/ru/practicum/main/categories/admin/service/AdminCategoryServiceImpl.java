package ru.practicum.main.categories.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.NewCategoryDto;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.categories.repository.CategoryRepository;
import ru.practicum.main.exception.NotFoundException;

import static ru.practicum.main.categories.mapper.CategoryMapper.mapFromCategoryToCategoryDto;
import static ru.practicum.main.categories.mapper.CategoryMapper.mapFromNewCategoryDtoToCategory;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return mapFromCategoryToCategoryDto(categoryRepository.save(mapFromNewCategoryDtoToCategory(newCategoryDto)));
    }

    @Override
    public void deleteCategory(int catId) {
        categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category with id:" + catId + " is not found."));
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto changeCategory(int catId, CategoryDto categoryDto) {
        Category outdatedCategory = categoryRepository.findById(catId).orElseThrow(/*() -> new NotFoundException("Category with id:" + catId + " is not found.")*/);
        if (categoryDto.getName() != null) {
            outdatedCategory.setName(categoryDto.getName());
        }
        return mapFromCategoryToCategoryDto(categoryRepository.save(outdatedCategory));
    }

    @Transactional(readOnly = true)
    @Override
    public Category findCategoryById(int catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category with id:" + catId + " is not found."));
    }
}
