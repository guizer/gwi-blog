package org.gwi.blog.controller;

import com.google.gson.Gson;
import java.util.List;
import org.gwi.blog.TestConfiguration;
import org.gwi.blog.dto.ArticleDto;
import org.gwi.blog.dto.CategoryDto;
import org.gwi.blog.dto.PagedArticles;
import org.gwi.blog.exception.ArticleNotFound;
import org.gwi.blog.service.IArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(TestConfiguration.class)
@WebMvcTest(controllers = ArticleRestController.class)
@AutoConfigureMockMvc
public class TestArticleRestController {

    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IArticleService articleService;

    @Test
    public void testGetArticleRespondWith200() throws Exception {
        ArticleDto expectedArticle = ArticleDto.builder()
            .id(1)
            .title("title")
            .content("content")
            .build();
        Mockito.when(articleService.getArticle(1)).thenReturn(expectedArticle);
        mockMvc.perform(MockMvcRequestBuilders.get(ArticleRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedArticle)));
    }

    @Test
    public void testGetArticleRespondWith404WhenArticleNotExist() throws Exception {
        Mockito.when(articleService.getArticle(1)).thenThrow(new ArticleNotFound(1));
        mockMvc.perform(MockMvcRequestBuilders.get(ArticleRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetArticlesRespondWith200() throws Exception {
        List<ArticleDto> articles = List.of(
            ArticleDto.builder()
                .id(1)
                .title("title")
                .content("content")
                .build(),
            ArticleDto.builder()
                .id(1)
                .title("title")
                .content("content")
                .build()
        );
        PagedArticles expectedPagedArticles = new PagedArticles(articles, 5, 10);
        Mockito.when(articleService.getArticles(1, 2)).thenReturn(expectedPagedArticles);
        mockMvc.perform(MockMvcRequestBuilders.get(ArticleRestController.NAMESPACE)
                .param("page", "1")
                .param("pageSize", "2")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedPagedArticles)));
    }

    @Test
    public void testGetArticlesByCategoryIdRespondWith200() throws Exception {
        CategoryDto category = new CategoryDto(10, "categoryName", "categorySlug");
        List<ArticleDto> articles = List.of(
            ArticleDto.builder()
                .id(1)
                .title("title")
                .content("content")
                .category(category)
                .build(),
            ArticleDto.builder()
                .id(1)
                .title("title")
                .content("content")
                .category(category)
                .build()
        );
        PagedArticles expectedPagedArticles = new PagedArticles(articles, 5, 10);
        Mockito.when(articleService.getArticlesByCategoryId(category.getId(), 1, 2))
            .thenReturn(expectedPagedArticles);
        String restPath = ArticleRestController.NAMESPACE + "/category/" + category.getId();
            mockMvc.perform(MockMvcRequestBuilders.get(restPath)
                .param("page", "1")
                .param("pageSize", "2")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedPagedArticles)));
    }

}
