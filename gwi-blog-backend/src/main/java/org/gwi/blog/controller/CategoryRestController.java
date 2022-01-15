package org.gwi.blog.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.controller.request.CategoryCreationRequest;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.exception.CategoryNameAlreadyExist;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.exception.CategorySlugAlreadyExist;
import org.gwi.blog.security.Roles;
import org.gwi.blog.service.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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

    @Secured(Roles.ROLE_ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryCreationRequest createRequest) {
        log.info("[GWI-BLOG] Create category for name {}", createRequest.getName());
        return categoryService.createCategory(createRequest.getName(), createRequest.getSlug());
    }

    @Secured(Roles.ROLE_ADMIN)
    @PutMapping("/{categoryId}")
    public CategoryDto updateCategory(@PathVariable int categoryId,
                                      @RequestBody CategoryCreationRequest updateRequest) {
        log.info("[GWI-BLOG] Rename category of id {} to {}", categoryId, updateRequest);
        return categoryService.updateCategory(categoryId, updateRequest.getName(),
            updateRequest.getSlug());
    }

    @Secured(Roles.ROLE_ADMIN)
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
    @ExceptionHandler(CategoryNameAlreadyExist.class)
    public String handleException(CategoryNameAlreadyExist exception) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategorySlugAlreadyExist.class)
    public String handleException(CategorySlugAlreadyExist exception) {
        return exception.getMessage();
    }

}
