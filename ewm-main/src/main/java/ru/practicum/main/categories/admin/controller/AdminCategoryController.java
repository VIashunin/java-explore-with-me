package ru.practicum.main.categories.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.categories.admin.service.AdminCategoryService;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/categories")
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return adminCategoryService.createCategory(newCategoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/categories/{catId}")
    public void deleteCategory(@PathVariable int catId) {
        adminCategoryService.deleteCategory(catId);
    }

    @PatchMapping(path = "/categories/{catId}")
    public CategoryDto changeCategory(@PathVariable int catId, @RequestBody @Valid CategoryDto categoryDto) {
        return adminCategoryService.changeCategory(catId, categoryDto);
    }
}
