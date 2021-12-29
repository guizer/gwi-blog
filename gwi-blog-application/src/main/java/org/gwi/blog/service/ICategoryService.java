package org.gwi.blog.service;

import java.util.List;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.dto.CategoryDto;

public interface ICategoryService {

    /**
     * Retrieve all categories.
     */
    List<CategoryDto> getAllCategories();

    /**
     * Delete the category with the given id.
     *
     * @param categoryId the category id
     * @return the deleted category
     * @throws CategoryNotFound if the category does not exist
     */
    CategoryDto deleteCategory(int categoryId);

    /**
     * Create a category.
     */
    CategoryDto createCategory(String name);

    /**
     * Rename the category with the given id.
     *
     * @param categoryId the category id
     * @param newName    the new category name
     * @return the renamed category
     * @throws CategoryNotFound if the category does not exist
     */
    CategoryDto renameCategory(int categoryId, String newName);

}