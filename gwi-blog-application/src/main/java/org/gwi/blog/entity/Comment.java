package org.gwi.blog.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gwi.blog.dto.CommentDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = Comment.ENTITY_NAME)
public class Comment {

    static final String ENTITY_NAME = "GWI_BLOG_COMMENT";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder.Default
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Builder.Default
    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastModificationDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public CommentDto convertToDto() {
        return CommentDto.builder()
            .id(id)
            .content(content)
            .creationDate(creationDate.format(DateTimeFormatter.ISO_DATE_TIME))
            .lastModificationDate(lastModificationDate.format(DateTimeFormatter.ISO_DATE_TIME))
            .build();
    }

}
