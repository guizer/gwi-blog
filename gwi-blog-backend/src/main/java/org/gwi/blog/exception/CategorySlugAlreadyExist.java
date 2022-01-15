package org.gwi.blog.exception;

import java.io.Serial;

public class CategorySlugAlreadyExist extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 6133994871532065755L;

    private final String categorySlug;

    public CategorySlugAlreadyExist(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    @Override
    public String getMessage() {
        return "A category with the slug " + categorySlug + " already exists.";
    }

}
