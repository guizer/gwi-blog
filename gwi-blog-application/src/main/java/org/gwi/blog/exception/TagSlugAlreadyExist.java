package org.gwi.blog.exception;

import java.io.Serial;

public class TagSlugAlreadyExist extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = -1962296582279132188L;

    private final String slugName;

    public TagSlugAlreadyExist(String slugName) {
        super();
        this.slugName = slugName;
    }

    @Override
    public String getMessage() {
        return "A tag with the slug " + slugName + " already exists.";
    }

}
