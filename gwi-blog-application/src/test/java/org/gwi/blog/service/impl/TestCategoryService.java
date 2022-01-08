package org.gwi.blog.service.impl;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.entity.Category;
import org.gwi.blog.exception.CategoryNameAlreadyExist;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.exception.CategorySlugAlreadyExist;
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

    private static final String TV_SHOWS_NAME = "TV Shows";
    private static final String TV_SHOWS_SLUG = "tv-shows";

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    @Test
    public void testGetAllCategories() {
        Mockito.when(categoryRepository.findAll())
            .thenReturn(List.of(new Category(1, TV_SHOWS_NAME, TV_SHOWS_NAME)));

        List<CategoryDto> actualCategorys = categoryService.getAllCategories();

        Assertions.assertThat(actualCategorys)
            .isEqualTo(List.of(new CategoryDto(1, TV_SHOWS_NAME, TV_SHOWS_NAME)));
    }

    @Test(expected = CategoryNameAlreadyExist.class)
    public void testCreateCategoryThrowCategoryAlreadyExistWhenCategoryWithSameNameExist() {
        Mockito.when(categoryRepository.findByName(TV_SHOWS_NAME)).thenReturn(
            Optional.of(new Category(1, TV_SHOWS_NAME, TV_SHOWS_NAME)));

        categoryService.createCategory(TV_SHOWS_NAME, TV_SHOWS_NAME);
    }

    @Test
    public void testCreateCategory() {
        Mockito.when(categoryRepository.findByName(TV_SHOWS_NAME)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(Mockito.any()))
            .thenReturn(new Category(1, TV_SHOWS_NAME, TV_SHOWS_NAME));
        CategoryDto actualCategory = categoryService.createCategory(TV_SHOWS_NAME, TV_SHOWS_SLUG);

        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());

        Assertions.assertThat(categoryArgumentCaptor.getValue().getName())
            .isEqualTo(TV_SHOWS_NAME);
        Assertions.assertThat(actualCategory).isEqualTo(new CategoryDto(1, TV_SHOWS_NAME,
            TV_SHOWS_NAME));
    }

    @Test(expected = CategoryNotFound.class)
    public void testUpdateCategoryThrowCategoryNotFoundCategoryExceptionWhenCategoryNotExist() {
        Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findByName(TV_SHOWS_NAME)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findByName(TV_SHOWS_SLUG)).thenReturn(Optional.empty());

        categoryService.updateCategory(1, TV_SHOWS_NAME, TV_SHOWS_SLUG);
    }

    @Test(expected = CategoryNameAlreadyExist.class)
    public void testUpdateCategoryThrowCategoryAlreadyExistWhenCategoryWithSameNameExist() {
        Mockito.when(categoryRepository.findByName(TV_SHOWS_NAME))
            .thenReturn(Optional.of(new Category(2, TV_SHOWS_NAME, "oldSlug")));
        Mockito.when(categoryRepository.findBySlug(TV_SHOWS_SLUG)).thenReturn(Optional.empty());

        categoryService.updateCategory(1, TV_SHOWS_NAME, TV_SHOWS_SLUG);
    }

    @Test(expected = CategorySlugAlreadyExist.class)
    public void testUpdateCategoryThrowCategoryAlreadyExistWhenCategoryWithSameSlugExist() {
        Mockito.when(categoryRepository.findByName(TV_SHOWS_NAME)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findBySlug(TV_SHOWS_SLUG))
            .thenReturn(Optional.of(new Category(1, "oldName", TV_SHOWS_SLUG)));

        categoryService.updateCategory(1, TV_SHOWS_NAME, TV_SHOWS_SLUG);
    }

    @Test
    public void testUpdateCategory() {
        Mockito.when(categoryRepository.findById(1))
            .thenReturn(Optional.of(new Category(1, "oldName", "oldSlug")));
        Mockito.when(categoryRepository.findByName(TV_SHOWS_NAME)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findBySlug(TV_SHOWS_SLUG)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(Mockito.any()))
            .thenReturn(new Category(1, TV_SHOWS_NAME, TV_SHOWS_SLUG));

        CategoryDto actualCategory =
            categoryService.updateCategory(1, TV_SHOWS_NAME, TV_SHOWS_SLUG);

        Assertions.assertThat(actualCategory)
            .isEqualTo(new CategoryDto(1, TV_SHOWS_NAME, TV_SHOWS_SLUG));
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());
        Assertions.assertThat(categoryArgumentCaptor.getValue().getId()).isEqualTo(1);
        Assertions.assertThat(categoryArgumentCaptor.getValue().getName())
            .isEqualTo(TV_SHOWS_NAME);
    }

    @Test(expected = CategoryNotFound.class)
    public void testRemoveCategoryThrowCategoryNotFoundCategoryExceptionWhenCategoryNotExist() {
        Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        categoryService.deleteCategory(1);
    }

    @Test
    public void testRemoveCategoryWhenCategoryExist() {
        Category categoryToRemove = new Category(1, TV_SHOWS_NAME, TV_SHOWS_SLUG);
        Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.of(categoryToRemove));

        CategoryDto actualCategory = categoryService.deleteCategory(1);

        Mockito.verify(categoryRepository, Mockito.times(1)).delete(categoryToRemove);
        Assertions.assertThat(actualCategory)
            .isEqualTo(new CategoryDto(1, TV_SHOWS_NAME, TV_SHOWS_SLUG));
    }

}
