package org.gwi.blog.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.google.gson.Gson;
import java.util.List;
import org.gwi.blog.TestConfiguration;
import org.gwi.blog.controller.request.CategoryCreationRequest;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.exception.CategoryNameAlreadyExist;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.exception.CategorySlugAlreadyExist;
import org.gwi.blog.security.Roles;
import org.gwi.blog.service.ICategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(TestConfiguration.class)
@WebMvcTest(controllers = CategoryRestController.class)
@AutoConfigureMockMvc
public class TestCategoryRestController {

    private static final String SPORT_CATEGORY_NAME = "Sport";
    private static final String SPORT_CATEGORY_SLUG = "sport";

    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICategoryService categoryService;

    @Test
    public void testGetAllCategoriesRespondWith200() throws Exception {
        List<CategoryDto> categories =
            List.of(new CategoryDto(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG));
        Mockito.when(categoryService.getAllCategories()).thenReturn(categories);
        mockMvc.perform(MockMvcRequestBuilders.get(CategoryRestController.NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(categories)));
    }

    @WithMockUser(roles = Roles.USER)
    @Test
    public void testCreateCategoryRespondWith403WhenCategoryUserIsNotAdmin() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG);
        Mockito.when(categoryService.createCategory(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))
            .thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.post(CategoryRestController.NAMESPACE)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(roles = Roles.ADMIN)
    @Test
    public void testCreateCategoryRespondWith201WhenCategoryNotExist() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG);
        Mockito.when(categoryService.createCategory(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))
            .thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.post(CategoryRestController.NAMESPACE)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedCategory)));
    }

    @WithMockUser(roles = Roles.ADMIN)
    @Test
    public void testCreateCategoryRespondWith400WhenCategoryExist() throws Exception {
        Mockito.when(categoryService.createCategory(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))
            .thenThrow(CategoryNameAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.post(CategoryRestController.NAMESPACE)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @WithMockUser(roles = Roles.USER)
    @Test
    public void testUpdateCategoryRespondWith403WhenUserIsNotAdmin() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG);
        Mockito.when(categoryService.updateCategory(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))
            .thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.put(CategoryRestController.NAMESPACE + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(roles = Roles.ADMIN)
    @Test
    public void testUpdateCategoryRespondWith200WhenCategoryExist() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG);
        Mockito.when(categoryService.updateCategory(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))
            .thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.put(CategoryRestController.NAMESPACE + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedCategory)));
    }

    @WithMockUser(roles = Roles.ADMIN)
    @Test
    public void testUpdateCategoryRespondWith404WhenCategoryNotExist() throws Exception {
        Mockito.when(categoryService.updateCategory(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))
            .thenThrow(CategoryNotFound.class);
        mockMvc.perform(MockMvcRequestBuilders.put(CategoryRestController.NAMESPACE + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @WithMockUser(roles = Roles.ADMIN)
    @Test
    public void testUpdateCategoryRespondWith400WhenNewNameAlreadyExist() throws Exception {
        Mockito.when(categoryService.updateCategory(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))
            .thenThrow(CategoryNameAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.put(CategoryRestController.NAMESPACE + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @WithMockUser(roles = Roles.ADMIN)
    @Test
    public void testUpdateCategoryRespondWith400WhenNewSlugAlreadyExist() throws Exception {
        Mockito.when(categoryService.updateCategory(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))
            .thenThrow(CategorySlugAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.put(CategoryRestController.NAMESPACE + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @WithMockUser(roles = Roles.USER)
    @Test
    public void testDeleteCategoryRespondWith403WhenUserIsNotAdmin() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG);
        Mockito.when(categoryService.deleteCategory(1)).thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.delete(CategoryRestController.NAMESPACE + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(roles = Roles.ADMIN)
    @Test
    public void testDeleteCategoryRespondWith200WhenCategoryExist() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG);
        Mockito.when(categoryService.deleteCategory(1)).thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.delete(CategoryRestController.NAMESPACE + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY_NAME, SPORT_CATEGORY_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedCategory)));
    }

    @WithMockUser(roles = Roles.ADMIN)
    @Test
    public void testDeleteCategoryRespondWith404WhenCategoryNotExist() throws Exception {
        Mockito.when(categoryService.deleteCategory(1)).thenThrow(CategoryNotFound.class);
        mockMvc.perform(MockMvcRequestBuilders.delete(CategoryRestController.NAMESPACE + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
