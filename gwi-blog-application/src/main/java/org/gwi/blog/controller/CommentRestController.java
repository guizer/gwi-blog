package org.gwi.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.controller.request.CommentCreationRequest;
import org.gwi.blog.dto.CommentDto;
import org.gwi.blog.exception.CommentNotFound;
import org.gwi.blog.service.ICommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = CommentRestController.NAMESPACE)
@RestController
public class CommentRestController {

    static final String NAMESPACE = "/api/v1/comments";

    private final ICommentService commentService;

    public CommentRestController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable int commentId,
                                    @RequestBody CommentCreationRequest updateRequest) {
        log.info("[GWI-BLOG] Update comment of id {} with content of length {}", commentId,
            updateRequest.getContent().length());
        return commentService.updateComment(commentId, updateRequest.getContent());
    }

    @DeleteMapping("/{commentId}")
    public CommentDto deleteComment(@PathVariable int commentId) {
        log.info("[GWI-BLOG] Delete comment of id {}", commentId);
        return commentService.deleteComment(commentId);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(CommentNotFound.class)
    public String handleException(CommentNotFound exception) {
        return exception.getMessage();
    }

}
