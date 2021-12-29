package org.gwi.blog.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedComments {

    private List<CommentDto> comments;
    private int totalPages;
    private int totalElements;

}
