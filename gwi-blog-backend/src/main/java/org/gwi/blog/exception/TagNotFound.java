package org.gwi.blog.exception;

import java.io.Serial;

public class TagNotFound extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 7486273583810227901L;

    private final int tagId;

    public TagNotFound(int tagId) {
        super();
        this.tagId = tagId;
    }

    @Override
    public String getMessage() {
        return "The tag " + tagId + " does not exist.";
    }

}
