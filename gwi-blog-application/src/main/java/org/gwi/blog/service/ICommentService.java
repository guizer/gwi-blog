package org.gwi.blog.service;

import org.gwi.blog.exception.CommentNotFound;
import org.gwi.blog.dto.CommentDto;

public interface ICommentService {

    /**
     * Delete the comment with the given id.
     *
     * @param commentId the comment id
     * @return the deleted comment
     * @throws CommentNotFound if the comment does not exist
     */
    CommentDto deleteComment(int commentId);

    /**
     * Update the comment with the given id.
     *
     * @param commentId  the comment id
     * @param newContent the new comment content
     * @return the updated comment
     * @throws CommentNotFound if the comment does not exist
     */
    CommentDto updateComment(int commentId, String newContent);

}
