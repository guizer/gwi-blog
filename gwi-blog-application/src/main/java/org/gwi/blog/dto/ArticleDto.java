package org.gwi.blog.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

    private int id;
    private String title;
    private String content;
    private String publishedAt;
    private String lastModifiedAt;
    private CategoryDto category;
    @Builder.Default
    private List<TagDto> tags = new ArrayList<>();
    private String author;

}
