package org.gwi.blog.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.gwi.blog.dto.ArticleDto;
import org.gwi.blog.entity.Article;
import org.gwi.blog.entity.Category;
import org.gwi.blog.exception.ArticleNotFound;
import org.gwi.blog.repository.ArticleRepository;
import org.gwi.blog.repository.CategoryRepository;
import org.gwi.blog.repository.CommentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestArticleService {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test(expected = ArticleNotFound.class)
    public void testGetArticleNotFoundExceptionWhenArticleNotExist() {
        Mockito.when(articleRepository.findById(1)).thenReturn(Optional.empty());

        articleService.getArticle(1);
    }

    @Test
    public void testGetArticleReturnArticleWhenArticleExist() {
        LocalDateTime creationDate = LocalDateTime.now();
        Article article = Article.builder()
            .id(1)
            .category(new Category(1, "test"))
            .title("title")
            .content("content")
            .creationDate(creationDate)
            .lastModificationDate(creationDate)
            .build();
        Mockito.when(articleRepository.findById(1)).thenReturn(Optional.of(article));

        ArticleDto actualArticle = articleService.getArticle(1);

        Assertions.assertThat(actualArticle).isEqualTo(article.convertToDto());
    }

    @Test(expected = ArticleNotFound.class)
    public void testDeleteArticleNotFoundExceptionWhenArticleNotExist() {
        Mockito.when(articleRepository.findById(1)).thenReturn(Optional.empty());

        articleService.deleteArticle(1);
    }

    @Test
    public void testDeleteArticleWhenArticleExist() {
        LocalDateTime creationDate = LocalDateTime.now();
        Article articleToRemove = Article.builder()
            .id(1)
            .title("title")
            .content("content")
            .category(new Category(1, "test"))
            .creationDate(creationDate)
            .lastModificationDate(creationDate)
            .build();
        Mockito.when(articleRepository.findById(1))
            .thenReturn(Optional.of(articleToRemove));

        ArticleDto articleDto = articleService.deleteArticle(1);

        Mockito.verify(articleRepository, Mockito.times(1)).delete(articleToRemove);
        Assertions.assertThat(articleDto).isEqualTo(articleToRemove.convertToDto());
    }

}
