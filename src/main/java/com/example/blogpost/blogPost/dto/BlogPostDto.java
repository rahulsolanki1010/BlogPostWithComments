package com.example.blogpost.blogPost.dto;

import com.example.blogpost.category.dto.CategoryDto;
import com.example.blogpost.comment.dto.CommentDto;
import com.example.blogpost.tag.dto.TagDto;

import java.util.List;

public class BlogPostDto {
    private Long blogPostId;
    private Long userId;
    private String title;
    private String content;
    private List<TagDto> tags;
    private List<CategoryDto> categories;
    private List<CommentDto> comments;
    public Long getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(Long blogPostId) {
        this.blogPostId = blogPostId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
}
