package org.gwi.blog.exception;

import java.io.Serial;

public class ArticleNotFound extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 580070876776486843L;

    private final int articleId;

    public ArticleNotFound(int articleId) {
        super();
        this.articleId = articleId;
    }

    @Override
    public String getMessage() {
        return "The article " + articleId + " does not exist.";
    }

}
