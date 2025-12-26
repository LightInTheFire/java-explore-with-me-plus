package ru.practicum.category.controller;

import java.util.Collection;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Transactional(readOnly = true)
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> getAllCategoriesPaged(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("View categories page by page requested from={}, size={}", from, size);
        return categoryService.getAllCategoriesPaged(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        log.info("View category with id={} requested", id);
        return categoryService.getCategoryById(id);
    }
}
