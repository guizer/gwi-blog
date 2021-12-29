package org.gwi.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.controller.request.CommentCreationRequest;
import org.gwi.blog.dto.ArticleDto;
import org.gwi.blog.dto.CommentDto;
import org.gwi.blog.dto.PagedArticles;
import org.gwi.blog.dto.PagedComments;
import org.gwi.blog.exception.ArticleNotFound;
import org.gwi.blog.service.ArticleCreationRequest;
import org.gwi.blog.service.IArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = ArticleRestController.NAMESPACE)
@RestController
public class ArticleRestController {

    static final String NAMESPACE = "/api/v1/articles";

    private final IArticleService articleService;

    public ArticleRestController(IArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{articleId}")
    public ArticleDto getArticle(@PathVariable int articleId) {
        log.info("[GWI-BLOG] Retrieve article for id {}", articleId);
        return articleService.getArticle(articleId);
    }

    @GetMapping
    public PagedArticles getArticles(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "20") int pageSize) {
        log.info("[GWI-BLOG] Retrieve articles for page {} and page size {}", page, pageSize);
        return articleService.getArticles(page, pageSize);
    }

    @PostMapping
    public ArticleDto createArticle(ArticleCreationRequest creationRequest) {
        log.info("[GWI-BLOG] Create article for request {}", creationRequest);
        return articleService.createArticle(creationRequest);
    }

    @PutMapping("/{articleId}")
    public ArticleDto updateArticle(@PathVariable int articleId,
                                    @RequestBody ArticleCreationRequest updateRequest) {
        log.info("[GWI-BLOG] Update article of id {} request {}", articleId, updateRequest);
        return articleService.updateArticle(articleId, updateRequest);
    }

    @DeleteMapping("/{articleId}")
    public ArticleDto deleteArticle(@PathVariable int articleId) {
        log.info("[GWI-BLOG] Delete article of id {}", articleId);
        return articleService.deleteArticle(articleId);
    }

    @GetMapping("/articles/{articleId}/comments")
    public PagedComments getComments(@PathVariable int articleId,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "20") int pageSize) {
        log.info("[GWI-BLOG] Retrieve comments for article {} page {} and page size {}", articleId,
            page, pageSize);
        return articleService.getComments(articleId, page, pageSize);
    }

    @PostMapping("/articles/{articleId}/comments")
    public CommentDto createComment(@PathVariable int articleId,
                                    @RequestBody CommentCreationRequest creationRequest) {
        log.info("[GWI-BLOG] Create comment for article {} and content of length {}", articleId,
            creationRequest.getContent().length());
        return articleService.createComment(articleId, creationRequest.getContent());
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ArticleNotFound.class)
    public String handleException(ArticleNotFound exception) {
        return exception.getMessage();
    }

}
