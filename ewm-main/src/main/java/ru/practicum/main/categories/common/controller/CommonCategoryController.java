package ru.practicum.main.categories.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.categories.common.service.CommonCategoryService;
import ru.practicum.main.categories.dto.CategoryDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommonCategoryController {
    private final CommonCategoryService commonCategoryService;

    @GetMapping(path = "/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return commonCategoryService.getCategories(from, size);
    }

    @GetMapping(path = "/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable int catId) {
        return commonCategoryService.getCategoryById(catId);
    }
}
