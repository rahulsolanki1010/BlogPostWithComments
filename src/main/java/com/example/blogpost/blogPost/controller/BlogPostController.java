package com.example.blogpost.blogPost.controller;

import com.example.blogpost.blogPost.dto.BlogPostDto;
import com.example.blogpost.blogPost.service.BlogPostService;
import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.user.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.blogpost.common.constant.CmnConstants.getLoggedInUser;

@RestController
@RequestMapping("/")
public class BlogPostController {
    @Autowired
    BlogPostService blogPostService;

    @PostMapping("save")
    ResponseEntity<SuccessResponse> saveBlog(@RequestBody BlogPostDto blogPostDto){
        User user = getLoggedInUser();
        blogPostDto.setUserId(user.getId());
        return blogPostService.saveBlog(blogPostDto);
    }

    @GetMapping("/")
    public ResponseEntity getAllBlogs(){
        User user = getLoggedInUser();
        return blogPostService.getAllBlogsForUser(user.getId());
    }

    @PutMapping("/{blogPostId}")
    public ResponseEntity updateBlog(@PathVariable Long blogPostId,@RequestBody @Valid BlogPostDto blogPost){
        User user = getLoggedInUser();
        return blogPostService.updateBlogById(user.getId(),blogPost,blogPostId);
    }

    @DeleteMapping("/{blogPostId}")
    public ResponseEntity deleteBlog(@PathVariable Long blogPostId){
        User user = getLoggedInUser();
        return blogPostService.deleteBlogById(blogPostId,user.getId());
    }
}
