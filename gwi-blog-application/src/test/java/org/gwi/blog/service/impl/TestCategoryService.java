package org.gwi.blog.service.impl;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.entity.Category;
import org.gwi.blog.exception.CategoryAlreadyExist;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestCategoryService {

    private static final String CATEGORY_FRESH = "fresh";

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    @Test
    public void testGetAllCategories() {
        Mockito.when(categoryRepository.findAll())
            .thenReturn(List.of(new Category(1, CATEGORY_FRESH)));

        List<CategoryDto> actualCategorys = categoryService.getAllCategories();

        Assertions.assertThat(actualCategorys)
            .isEqualTo(List.of(new CategoryDto(1, CATEGORY_FRESH)));
    }

    @Test(expected = CategoryAlreadyExist.class)
    public void testCreateCategoryThrowCategoryAlreadyExistWhenCategoryWithSameNameExist() {
        Mockito.when(categoryRepository.findByName(CATEGORY_FRESH)).thenReturn(
            Optional.of(new Category(1, CATEGORY_FRESH)));

        categoryService.createCategory(CATEGORY_FRESH);
    }

    @Test
    public void testCreateCategory() {
        Mockito.when(categoryRepository.findByName(CATEGORY_FRESH)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(Mockito.any()))
            .thenReturn(new Category(1, CATEGORY_FRESH));
        CategoryDto actualCategory = categoryService.createCategory(CATEGORY_FRESH);

        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());

        Assertions.assertThat(categoryArgumentCaptor.getValue().getName())
            .isEqualTo(CATEGORY_FRESH);
        Assertions.assertThat(actualCategory).isEqualTo(new CategoryDto(1, CATEGORY_FRESH));
    }

    @Test(expected = CategoryNotFound.class)
    public void testRenameCategoryThrowCategoryNotFoundCategoryExceptionWhenCategoryNotExist() {
        Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findByName(CATEGORY_FRESH)).thenReturn(Optional.empty());

        categoryService.renameCategory(1, CATEGORY_FRESH);
    }

    @Test(expected = CategoryAlreadyExist.class)
    public void testRenameCategoryThrowCategoryAlreadyExistWhenCategoryWithSameNameExist() {
        Mockito.when(categoryRepository.findByName(CATEGORY_FRESH))
            .thenReturn(Optional.of(new Category(2, CATEGORY_FRESH)));

        categoryService.renameCategory(1, CATEGORY_FRESH);
    }

    @Test
    public void testRenameCategory() {
        Mockito.when(categoryRepository.findById(1))
            .thenReturn(Optional.of(new Category(1, "toRename")));
        Mockito.when(categoryRepository.findByName(CATEGORY_FRESH)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(Mockito.any()))
            .thenReturn(new Category(1, CATEGORY_FRESH));

        CategoryDto actualCategory = categoryService.renameCategory(1, CATEGORY_FRESH);

        Assertions.assertThat(actualCategory).isEqualTo(new CategoryDto(1, CATEGORY_FRESH));
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());
        Assertions.assertThat(categoryArgumentCaptor.getValue().getId()).isEqualTo(1);
        Assertions.assertThat(categoryArgumentCaptor.getValue().getName())
            .isEqualTo(CATEGORY_FRESH);
    }

    @Test(expected = CategoryNotFound.class)
    public void testRemoveCategoryThrowCategoryNotFoundCategoryExceptionWhenCategoryNotExist() {
        Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        categoryService.deleteCategory(1);
    }

    @Test
    public void testRemoveCategoryWhenCategoryExist() {
        Category categoryToRemove = new Category(1, "testCategory");
        Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.of(categoryToRemove));

        CategoryDto actualCategory = categoryService.deleteCategory(1);

        Mockito.verify(categoryRepository, Mockito.times(1)).delete(categoryToRemove);
        Assertions.assertThat(actualCategory).isEqualTo(new CategoryDto(1, "testCategory"));
    }

}
