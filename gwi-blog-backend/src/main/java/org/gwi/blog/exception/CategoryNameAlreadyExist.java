package org.gwi.blog.exception;

import java.io.Serial;

public class CategoryNameAlreadyExist extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 6902544625382069437L;

    private final String categoryName;

    public CategoryNameAlreadyExist(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String getMessage() {
        return "A category with the name " + categoryName + " already exists.";
    }

}
