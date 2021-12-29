package org.gwi.blog.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.controller.request.CategoryCreationRequest;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.exception.CategoryAlreadyExist;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.service.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = CategoryRestController.NAMESPACE)
@RestController
public class CategoryRestController {

    static final String NAMESPACE = "/api/v1/categories";

    private final ICategoryService categoryService;

    public CategoryRestController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        log.info("[GWI-BLOG] Retrieve all categories");
        return categoryService.getAllCategories();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryCreationRequest createRequest) {
        log.info("[GWI-BLOG] Create category for name {}", createRequest.getName());
        return categoryService.createCategory(createRequest.getName());
    }

    @PutMapping("/{categoryId}")
    public CategoryDto renameCategory(@PathVariable int categoryId,
                                      @RequestBody CategoryCreationRequest renameRequest) {
        log.info("[GWI-BLOG] Rename category of id {} to {}", categoryId, renameRequest.getName());
        return categoryService.renameCategory(categoryId, renameRequest.getName());
    }

    @DeleteMapping("/{categoryId}")
    public CategoryDto deleteCategory(@PathVariable int categoryId) {
        log.info("[GWI-BLOG] Delete category of id {}", categoryId);
        return categoryService.deleteCategory(categoryId);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFound.class)
    public String handleException(CategoryNotFound exception) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryAlreadyExist.class)
    public String handleException(CategoryAlreadyExist exception) {
        return exception.getMessage();
    }

}