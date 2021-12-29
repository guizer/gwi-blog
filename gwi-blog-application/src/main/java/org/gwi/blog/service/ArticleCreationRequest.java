package org.gwi.blog.service;

import java.util.List;
import lombok.Data;

@Data
public class ArticleCreationRequest {

    private String title;
    private String content;
    private int categoryId;
    private List<String> tagNames;

}
