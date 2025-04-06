package com.example.blogpost.comment.service;

import com.example.blogpost.comment.dto.CommentDto;
import com.example.blogpost.response.SuccessResponse;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<SuccessResponse> saveComment(CommentDto commentDto);
    ResponseEntity getAllCommentsForBlogpost(Long blogpostId);
}
