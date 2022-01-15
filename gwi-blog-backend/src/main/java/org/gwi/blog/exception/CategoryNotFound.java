package org.gwi.blog.exception;

import java.io.Serial;

public class CategoryNotFound extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 150610892043751264L;

    private final int categoryId;

    public CategoryNotFound(int categoryId) {
        super();
        this.categoryId = categoryId;
    }

    @Override
    public String getMessage() {
        return "The category " + categoryId + " does not exist.";
    }

}
