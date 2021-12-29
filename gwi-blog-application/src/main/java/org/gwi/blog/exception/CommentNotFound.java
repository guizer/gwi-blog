package org.gwi.blog.exception;

import java.io.Serial;

public class CommentNotFound extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 1106613675925045372L;

    private final int commentId;

    public CommentNotFound(int commentId) {
        super();
        this.commentId = commentId;
    }

    @Override
    public String getMessage() {
        return "The comment " + commentId + " does not exist.";
    }

}
