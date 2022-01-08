package org.gwi.blog.service.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.entity.Category;
import org.gwi.blog.exception.CategoryNameAlreadyExist;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.exception.CategorySlugAlreadyExist;
import org.gwi.blog.repository.CategoryRepository;
import org.gwi.blog.service.ICategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(Category::convertToDto).toList();
    }

    @Transactional
    @Override
    public CategoryDto deleteCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFound(categoryId));
        categoryRepository.delete(category);
        return category.convertToDto();
    }

    @Transactional
    @Override
    public CategoryDto createCategory(String name, String slug) {
        categoryRepository.findByName(name).ifPresent(categoryFound -> {
            throw new CategoryNameAlreadyExist(name);
        });
        categoryRepository.findBySlug(slug).ifPresent(categoryFound -> {
            throw new CategorySlugAlreadyExist(slug);
        });
        Category category = new Category();
        category.setName(name);
        category.setSlug(slug);
        return categoryRepository.save(category).convertToDto();
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(int categoryId, String newName, String newSlug) {
        categoryRepository.findByName(newName).ifPresent(tagFound -> {
            throw new CategoryNameAlreadyExist(newName);
        });
        categoryRepository.findBySlug(newSlug).ifPresent(tagFound -> {
            throw new CategorySlugAlreadyExist(newSlug);
        });
        Category categoryToRename = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFound(categoryId));
        categoryToRename.setName(newName);
        return categoryRepository.save(categoryToRename).convertToDto();
    }

}
