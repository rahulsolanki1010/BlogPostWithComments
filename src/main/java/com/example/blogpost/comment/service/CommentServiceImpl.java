package com.example.blogpost.comment.service;

import com.example.blogpost.blogPost.repo.BlogPostRepo;
import com.example.blogpost.comment.dto.CommentDto;
import com.example.blogpost.comment.model.Comment;
import com.example.blogpost.comment.repo.CommentRepo;
import com.example.blogpost.exception.ApplicationException;
import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.user.dto.UserDTO;
import com.example.blogpost.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.blogpost.common.constant.CmnConstants.getLoggedInUser;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    BlogPostRepo blogPostRepo;
    @Autowired
    CommentRepo commentRepo;

    @Override
    public ResponseEntity<SuccessResponse> saveComment(CommentDto commentDto) {
        Comment comment = new Comment();
        User loggedInUser= getLoggedInUser();
        comment.setBlogPost(blogPostRepo.findById(commentDto.getBlogpostId()).orElseThrow(() -> new ApplicationException("No blog post Found With Id " + commentDto.getBlogpostId(), HttpStatus.NOT_FOUND)));
        comment.setCommentText(commentDto.getCommentText());
        comment.setUser(loggedInUser);
        if(commentDto.getParentCommentId() !=null) {
            Optional<Comment> parentComment = commentRepo.findById(commentDto.getParentCommentId());
            comment.setParentCommentId(parentComment.isPresent() ? parentComment.get().getCommentId() : -1L);
        }else{
            comment.setParentCommentId(-1L);
        }
        commentRepo.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(HttpStatus.CREATED, "Comment Saved Successfully"));
    }

    @Override
    public ResponseEntity getAllCommentsForBlogpost(Long blogpostId) {
        Optional<List<Comment>> tempLstComments = commentRepo.findByBlogPostBlogPostId(blogpostId);
        List<CommentDto> lstComments = new ArrayList<CommentDto>();
        if (tempLstComments.isPresent() && !tempLstComments.get().isEmpty()) {
            lstComments = tempLstComments.get().stream().map(comment ->
                    {
                        CommentDto commentDto = new CommentDto();
                        commentDto.setCommentId(comment.getCommentId());
                        commentDto.setCommentText(comment.getCommentText());
                        commentDto.setBlogpostId(comment.getBlogPost().getBlogPostId());
                        commentDto.setParentCommentId(comment.getParentCommentId());
                        UserDTO comUser = new UserDTO();
                        comUser.setUsername(comment.getUser().getUsername());
                        commentDto.setUserDTO(comUser);
                        return commentDto;
                    }
            ).collect(Collectors.toList());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(HttpStatus.OK, "success",lstComments));
    }
}
