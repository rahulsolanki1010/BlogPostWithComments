package com.example.blogpost.comment.dto;

import com.example.blogpost.user.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public class CommentDto {
    private Long commentId;
    @NotNull
    @Column(name = "comment_text", length = 500)
    private String commentText;

    private Long blogpostId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDTO userDTO;

    private Long parentCommentId;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public @NotNull String getCommentText() {
        return commentText;
    }

    public void setCommentText(@NotNull String commentText) {
        this.commentText = commentText;
    }

    public Long getBlogpostId() {
        return blogpostId;
    }

    public void setBlogpostId(Long blogpostId) {
        this.blogpostId = blogpostId;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
