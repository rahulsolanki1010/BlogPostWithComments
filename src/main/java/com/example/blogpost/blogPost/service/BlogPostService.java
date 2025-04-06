package com.example.blogpost.blogPost.service;

import com.example.blogpost.blogPost.dto.BlogPostDto;
import com.example.blogpost.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface BlogPostService {
    ResponseEntity<SuccessResponse> saveBlog(BlogPostDto blogPostDto);

    ResponseEntity getAllBlogsForUser(Long id);

    ResponseEntity updateBlogById(Long id, @Valid BlogPostDto blogPost, Long blogPostId);

    ResponseEntity deleteBlogById(Long blogPostId, Long id);

    ResponseEntity getAllBlogs();

    ResponseEntity getAllBlogsByTags(String tag);

    ResponseEntity getAllBlogsByCategories(String category);
}
