package org.gwi.blog.repository;

import java.util.Optional;
import org.gwi.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByName(String name);

    Optional<Tag> findBySlug(String slug);


}
