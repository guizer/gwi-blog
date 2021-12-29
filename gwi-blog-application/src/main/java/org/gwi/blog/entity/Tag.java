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
import org.gwi.blog.dto.TagDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = Tag.ENTITY_NAME)
public class Tag {

    static final String ENTITY_NAME = "GWI_BLOG_TAG";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public TagDto convertToDto() {
        return new TagDto(id, name);
    }

}
