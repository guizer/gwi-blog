package org.gwi.blog.controller;

import com.google.gson.Gson;
import java.util.List;
import org.gwi.blog.TestConfiguration;
import org.gwi.blog.controller.request.CategoryCreationRequest;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.exception.CategoryAlreadyExist;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.service.ICategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(TestConfiguration.class)
@WebMvcTest(controllers = CategoryRestController.class)
@AutoConfigureMockMvc
public class TestCategoryRestController {

    private static final String SPORT_CATEGORY = "sport";

    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICategoryService categoryService;

    @Test
    public void testGetAllCategoriesRespondWith200() throws Exception {
        List<CategoryDto> categories = List.of(new CategoryDto(1, SPORT_CATEGORY));
        Mockito.when(categoryService.getAllCategories()).thenReturn(categories);
        mockMvc.perform(MockMvcRequestBuilders.get(CategoryRestController.NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(categories)));
    }

    @Test
    public void testCreateCategoryRespondWith201WhenCategoryNotExist() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY);
        Mockito.when(categoryService.createCategory(SPORT_CATEGORY)).thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.post(CategoryRestController.NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY))))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedCategory)));
    }

    @Test
    public void testCreateCategoryRespondWith400WhenCategoryExist() throws Exception {
        Mockito.when(categoryService.createCategory(SPORT_CATEGORY))
            .thenThrow(CategoryAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.post(CategoryRestController.NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testRenameCategoryRespondWith200WhenCategoryExist() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY);
        Mockito.when(categoryService.renameCategory(1, SPORT_CATEGORY))
            .thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.put(CategoryRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedCategory)));
    }

    @Test
    public void testRenameCategoryRespondWith404WhenCategoryNotExist() throws Exception {
        Mockito.when(categoryService.renameCategory(1, SPORT_CATEGORY))
            .thenThrow(CategoryNotFound.class);
        mockMvc.perform(MockMvcRequestBuilders.put(CategoryRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY))))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testRenameCategoryRespondWith400WhenNewNameAlreadyExist() throws Exception {
        Mockito.when(categoryService.renameCategory(1, SPORT_CATEGORY))
            .thenThrow(CategoryAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.put(CategoryRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testDeleteCategoryRespondWith200WhenCategoryExist() throws Exception {
        CategoryDto expectedCategory = new CategoryDto(1, SPORT_CATEGORY);
        Mockito.when(categoryService.deleteCategory(1)).thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.delete(CategoryRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(SPORT_CATEGORY))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedCategory)));
    }

    @Test
    public void testDeleteCategoryRespondWith404WhenCategoryNotExist() throws Exception {
        Mockito.when(categoryService.deleteCategory(1)).thenThrow(CategoryNotFound.class);
        mockMvc.perform(MockMvcRequestBuilders.delete(CategoryRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
