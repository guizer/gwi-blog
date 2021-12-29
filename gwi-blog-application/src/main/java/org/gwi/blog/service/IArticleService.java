package org.gwi.blog.service;

import org.gwi.blog.dto.ArticleDto;
import org.gwi.blog.dto.CommentDto;
import org.gwi.blog.dto.PagedArticles;
import org.gwi.blog.dto.PagedComments;
import org.gwi.blog.exception.ArticleNotFound;

public interface IArticleService {

    /**
     * Retrieve the article with the given id.
     *
     * @param articleId the article id
     * @return the found article
     * @throws ArticleNotFound if the article does not exist
     */
    ArticleDto getArticle(int articleId);

    /**
     * Retrieve the articles at the given page.
     */
    PagedArticles getArticles(int page, int pageSize);

    /**
     * Delete the article with the given id.
     *
     * @param articleId the article id
     * @return the deleted article
     * @throws ArticleNotFound if the article does not exist
     */
    ArticleDto deleteArticle(int articleId);

    /**
     * Create an article.
     */
    ArticleDto createArticle(ArticleCreationRequest creationRequest);

    /**
     * Update the article with the given id.
     *
     * @param updateRequest properties to update
     * @return the updated article
     * @throws ArticleNotFound if the article does not exist
     */
    ArticleDto updateArticle(int articleId, ArticleCreationRequest updateRequest);

    /**
     * Retrieve comments of the given article.
     */
    PagedComments getComments(int articleId, int page, int pageSize);

    /**
     * Create a comment for the given article.
     *
     * @param articleId the article id
     * @param content   the comment content
     * @return the created comment
     * @throws ArticleNotFound if the article does not exist
     */
    CommentDto createComment(int articleId, String content);

}
