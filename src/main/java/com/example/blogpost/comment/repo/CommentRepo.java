package com.example.blogpost.comment.repo;

import com.example.blogpost.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {
    Optional<List<Comment>> findByBlogPostBlogPostId(Long blogpostId);

    Optional<List<Comment>> findAllByBlogPostBlogPostId(Long blogPostId);
}
