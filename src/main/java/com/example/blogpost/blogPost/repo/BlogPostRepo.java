package com.example.blogpost.blogPost.repo;

import com.example.blogpost.blogPost.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepo extends JpaRepository<BlogPost, Long> {
    Optional<List<BlogPost>> findByUserId(Long id);

    List<BlogPost> findAllByCategoriesCategoryId(Long categoryId);

    List<BlogPost> findAllByTagsTagId(Long tagId);
}
