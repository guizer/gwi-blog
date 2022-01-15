package org.gwi.blog.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.gwi.blog.dto.ArticleDto;
import org.gwi.blog.dto.PagedArticles;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Test
    public void testGetArticles() {
        LocalDateTime publicationDate = LocalDateTime.now();
        List<Article> articles = List.of(Article.builder()
            .id(1)
            .category(new Category(1, "categoryName", "categorySlug"))
            .title("title")
            .content("content")
            .publishedAt(publicationDate)
            .lastModifiedAt(publicationDate)
            .build());
        Page<Article> articlesPage = new PageImpl<>(articles, Pageable.ofSize(1), 3);
        Mockito.when(articleRepository.findAll(PageRequest.of(0, 1)))
            .thenReturn(articlesPage);

        PagedArticles actualArticles = articleService.getArticles(1, 1);

        PagedArticles expectedArticles =
            new PagedArticles(articles.stream().map(Article::convertToDto).collect(
                Collectors.toList()), 3, 3);
        Assertions.assertThat(actualArticles).isEqualTo(expectedArticles);
    }

    @Test
    public void testGetArticlesByCategoryId() {
        LocalDateTime publicationDate = LocalDateTime.now();
        int categoryId = 10;
        List<Article> articles = List.of(Article.builder()
            .id(1)
            .category(new Category(categoryId, "categoryName", "categorySlug"))
            .title("title")
            .content("content")
            .publishedAt(publicationDate)
            .lastModifiedAt(publicationDate)
            .build());
        Page<Article> articlesPage = new PageImpl<>(articles, Pageable.ofSize(1), 3);
        Mockito.when(articleRepository.findByCategoryId(categoryId, PageRequest.of(0, 1)))
            .thenReturn(articlesPage);

        PagedArticles actualArticles = articleService.getArticlesByCategoryId(categoryId, 1, 1);

        PagedArticles expectedArticles =
            new PagedArticles(articles.stream().map(Article::convertToDto).collect(
                Collectors.toList()), 3, 3);
        Assertions.assertThat(actualArticles).isEqualTo(expectedArticles);
    }

    @Test(expected = ArticleNotFound.class)
    public void testGetArticleNotFoundExceptionWhenArticleNotExist() {
        Mockito.when(articleRepository.findById(1)).thenReturn(Optional.empty());

        articleService.getArticle(1);
    }

    @Test
    public void testGetArticleReturnArticleWhenArticleExist() {
        LocalDateTime publicationDate = LocalDateTime.now();
        Article article = Article.builder()
            .id(1)
            .category(new Category(1, "categoryName", "categorySlug"))
            .title("title")
            .content("content")
            .publishedAt(publicationDate)
            .lastModifiedAt(publicationDate)
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
        LocalDateTime publicationDate = LocalDateTime.now();
        Article articleToRemove = Article.builder()
            .id(1)
            .title("title")
            .content("content")
            .category(new Category(1, "categoryName", "categorySlug"))
            .publishedAt(publicationDate)
            .lastModifiedAt(publicationDate)
            .build();
        Mockito.when(articleRepository.findById(1))
            .thenReturn(Optional.of(articleToRemove));

        ArticleDto articleDto = articleService.deleteArticle(1);

        Mockito.verify(articleRepository, Mockito.times(1)).delete(articleToRemove);
        Assertions.assertThat(articleDto).isEqualTo(articleToRemove.convertToDto());
    }

}
