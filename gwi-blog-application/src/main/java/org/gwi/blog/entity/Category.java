package org.gwi.blog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gwi.blog.dto.CategoryDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = Category.ENTITY_NAME)
public class Category {

    static final String ENTITY_NAME = "GWI_BLOG_CATEGORY";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public CategoryDto convertToDto() {
        return new CategoryDto(id, name);
    }

}
