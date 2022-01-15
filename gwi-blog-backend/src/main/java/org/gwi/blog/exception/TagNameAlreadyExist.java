package org.gwi.blog.exception;

import java.io.Serial;

public class TagNameAlreadyExist extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = -1962296582279132188L;

    private final String tagName;

    public TagNameAlreadyExist(String tagName) {
        super();
        this.tagName = tagName;
    }

    @Override
    public String getMessage() {
        return "A tag with the name " + tagName + " already exists.";
    }

}
