package org.gwi.blog.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gwi.blog.dto.ArticleDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = Article.ENTITY_NAME)
public class Article {

    static final String ENTITY_NAME = "GWI_BLOG_ARTICLE";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastModificationDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    private String author;

    public ArticleDto convertToDto() {
        return ArticleDto.builder()
            .id(this.id)
            .title(this.title)
            .content(this.content)
            .creationDate(this.creationDate.format(DateTimeFormatter.ISO_DATE_TIME))
            .lastModificationDate(this.lastModificationDate.format(DateTimeFormatter.ISO_DATE_TIME))
            .category(this.category.convertToDto())
            .tags(this.tags.stream().map(Tag::convertToDto).toList())
            .author(this.author)
            .build();
    }

}
