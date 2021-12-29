package org.gwi.blog.service.impl;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.dto.CommentDto;
import org.gwi.blog.entity.Comment;
import org.gwi.blog.exception.CommentNotFound;
import org.gwi.blog.exception.TagNotFound;
import org.gwi.blog.repository.CommentRepository;
import org.gwi.blog.service.ICommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public CommentDto deleteComment(int commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFound(commentId));
        commentRepository.delete(comment);
        return comment.convertToDto();
    }

    @Transactional
    @Override
    public CommentDto updateComment(int commentId, String newContent) {
        Comment comment =
            commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFound(commentId));
        comment.setContent(newContent);
        comment.setLastModificationDate(LocalDateTime.now());
        return commentRepository.save(comment).convertToDto();
    }

}
