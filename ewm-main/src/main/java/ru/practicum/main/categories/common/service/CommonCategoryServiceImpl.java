package ru.practicum.main.categories.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.categories.repository.CategoryRepository;
import ru.practicum.main.exception.NotFoundException;

import java.util.List;

import static ru.practicum.main.categories.mapper.CategoryMapper.mapFromCategoryListToCategoryDtoList;
import static ru.practicum.main.categories.mapper.CategoryMapper.mapFromCategoryToCategoryDto;
import static ru.practicum.main.utilities.Paginate.paginate;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonCategoryServiceImpl implements CommonCategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = paginate(from, size);
        return mapFromCategoryListToCategoryDtoList(categoryRepository.findAll(page));
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoryById(int catId) {
        return mapFromCategoryToCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id:" + catId + " is not found.")));
    }

    @Transactional(readOnly = true)
    @Override
    public Category findCategoryById(int catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id:" + catId + " is not found."));
    }
}
