package com.example.blogpost.comment.controller;

import com.example.blogpost.comment.dto.CommentDto;
import com.example.blogpost.comment.service.CommentService;
import com.example.blogpost.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;
    @PostMapping("")
    ResponseEntity<SuccessResponse> saveComment(@RequestBody CommentDto commentDto){
        return this.commentService.saveComment(commentDto);
    }

    @GetMapping("/comments/{blogpostId}")
    public ResponseEntity getComment(@PathVariable Long blogpostId){
        return commentService.getAllCommentsForBlogpost(blogpostId);
    }
}
