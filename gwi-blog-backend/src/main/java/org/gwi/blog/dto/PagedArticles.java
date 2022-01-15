package org.gwi.blog.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedArticles {

    private List<ArticleDto> articles;
    private int totalPages;
    private long totalElements;

}
