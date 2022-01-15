package org.gwi.blog.dto;

import lombok.Data;

@Data
public class CategoryDto {

    private final int id;
    private final String name;
    private final String slug;

}
