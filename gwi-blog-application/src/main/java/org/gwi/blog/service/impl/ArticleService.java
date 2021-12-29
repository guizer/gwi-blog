package org.gwi.blog.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.dto.ArticleDto;
import org.gwi.blog.dto.CommentDto;
import org.gwi.blog.dto.PagedArticles;
import org.gwi.blog.dto.PagedComments;
import org.gwi.blog.entity.Article;
import org.gwi.blog.entity.Category;
import org.gwi.blog.entity.Comment;
import org.gwi.blog.exception.ArticleNotFound;
import org.gwi.blog.exception.CategoryNotFound;
import org.gwi.blog.repository.ArticleRepository;
import org.gwi.blog.repository.CategoryRepository;
import org.gwi.blog.repository.CommentRepository;
import org.gwi.blog.service.ArticleCreationRequest;
import org.gwi.blog.service.IArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ArticleService implements IArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    public ArticleService(ArticleRepository articleRepository,
                          CategoryRepository categoryRepository,
                          CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ArticleDto getArticle(int articleId) {
        return articleRepository.findById(articleId)
            .orElseThrow(() -> new ArticleNotFound(articleId))
            .convertToDto();
    }

    @Override
    public PagedArticles getArticles(int page, int pageSize) {
        Page<ArticleDto> articlePage = articleRepository.findAll(PageRequest.of(page, pageSize))
            .map(Article::convertToDto);
        return new PagedArticles(articlePage.getContent(), articlePage.getTotalPages(),
            articlePage.getNumberOfElements());
    }

    @Transactional
    @Override
    public ArticleDto deleteArticle(int articleId) {
        Article articleToDelete = articleRepository.findById(articleId)
            .orElseThrow(() -> new ArticleNotFound(articleId));
        articleRepository.delete(articleToDelete);
        return articleToDelete.convertToDto();
    }

    @Transactional
    @Override
    public ArticleDto createArticle(ArticleCreationRequest creationRequest) {
        Category category = categoryRepository.findById(creationRequest.getCategoryId())
            .orElseThrow(() -> new CategoryNotFound(creationRequest.getCategoryId()));
        Article articleToCreate = Article.builder()
            .title(creationRequest.getTitle())
            .content(creationRequest.getContent())
            .category(category)
            .build();
        return articleRepository.saveAndFlush(articleToCreate).convertToDto();
    }

    @Transactional
    @Override
    public ArticleDto updateArticle(int articleId, ArticleCreationRequest updateRequest) {
        Article articleToUpdate = articleRepository.findById(articleId)
            .orElseThrow(() -> new ArticleNotFound(articleId));
        Category category = categoryRepository.findById(updateRequest.getCategoryId())
            .orElseThrow(() -> new CategoryNotFound(updateRequest.getCategoryId()));
        articleToUpdate.setTitle(updateRequest.getTitle());
        articleToUpdate.setContent(updateRequest.getContent());
        articleToUpdate.setCategory(category);
        return articleRepository.saveAndFlush(articleToUpdate).convertToDto();
    }

    @Override
    public PagedComments getComments(int articleId, int page, int pageSize) {
        Page<CommentDto> commentPage =
            commentRepository.findByArticleId(articleId, PageRequest.of(page, pageSize))
                .map(Comment::convertToDto);
        return new PagedComments(commentPage.getContent(), commentPage.getTotalPages(),
            commentPage.getNumberOfElements());
    }

    @Transactional
    @Override
    public CommentDto createComment(int articleId, String content) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new ArticleNotFound(articleId));
        Comment comment = Comment.builder()
            .article(article)
            .content(content)
            .build();
        return commentRepository.saveAndFlush(comment).convertToDto();
    }

}
