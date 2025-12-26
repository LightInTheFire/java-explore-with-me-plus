package ru.practicum.category.controller;

import jakarta.validation.Valid;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdateCategoryDto;
import ru.practicum.category.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Create category requested {}", newCategoryDto);
        return categoryService.createCategory(newCategoryDto);
    }

    @Transactional
    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @PathVariable Long catId, @Valid @RequestBody UpdateCategoryDto updateCategoryDto) {
        log.info(
                "Update category with id={} requested, updated category {}",
                catId,
                updateCategoryDto);
        return categoryService.updateCategory(catId, updateCategoryDto);
    }

    @Transactional
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Delete category with id={} requested", catId);
        categoryService.deleteCategoryById(catId);
    }
}
