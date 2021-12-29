package org.gwi.blog.service.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.entity.Category;
import org.gwi.blog.exception.CategoryAlreadyExist;
import org.gwi.blog.exception.CategoryNotFound;
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
    public CategoryDto createCategory(String name) {
        categoryRepository.findByName(name).ifPresent(categoryFound -> {
            throw new CategoryAlreadyExist(name);
        });
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category).convertToDto();
    }

    @Transactional
    @Override
    public CategoryDto renameCategory(int categoryId, String newName) {
        categoryRepository.findByName(newName).ifPresent(tagFound -> {
            throw new CategoryAlreadyExist(newName);
        });
        Category categoryToRename = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFound(categoryId));
        categoryToRename.setName(newName);
        return categoryRepository.save(categoryToRename).convertToDto();
    }

}
