package org.gwi.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private int id;
    private String content;
    private String author;
    private String creationDate;
    private String lastModificationDate;

}
