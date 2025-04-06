package com.example.blogpost.tag.repo;

import com.example.blogpost.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag,Long> {
    Tag findByTagNameIgnoreCase(String tagName);
}
