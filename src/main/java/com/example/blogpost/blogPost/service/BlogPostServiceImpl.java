package com.example.blogpost.blogPost.service;

import com.example.blogpost.blogPost.dto.BlogPostDto;
import com.example.blogpost.blogPost.model.BlogPost;
import com.example.blogpost.blogPost.repo.BlogPostRepo;
import com.example.blogpost.category.dto.CategoryDto;
import com.example.blogpost.category.model.Category;
import com.example.blogpost.category.repo.CategoryRepo;
import com.example.blogpost.exception.ApplicationException;
import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.tag.dto.TagDto;
import com.example.blogpost.tag.model.Tag;
import com.example.blogpost.tag.repo.TagRepo;
import com.example.blogpost.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogPostServiceImpl implements BlogPostService{
    @Autowired
    BlogPostRepo blogPostRepo;
    @Autowired
    TagRepo tagRepo;
    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public ResponseEntity<SuccessResponse> saveBlog(BlogPostDto blogPostDto) {
        BlogPost blogpost = new BlogPost();
        blogpost.setContent(blogPostDto.getContent());
        blogpost.setTitle(blogPostDto.getTitle());
        blogpost.setUser(new User(blogPostDto.getUserId()));

        //code for adding tag
        List<Tag> tagList = new ArrayList<>();
        for(TagDto tagDto:blogPostDto.getTags()){
            Tag tag = tagRepo.findById(tagDto.getTagId()).orElseThrow(() -> new ApplicationException("Tag does not exist with id-> " + tagDto.getTagId(), HttpStatus.NOT_FOUND));
            tagList.add(tag);
        }
        blogpost.setTags(tagList);
        // adding category
        blogpost.setCategories(blogPostDto.getCategories().stream().map(category -> {
            return categoryRepo.findById(category.getCategoryId()).orElseThrow(() -> new ApplicationException("Category does not exist with id-> " + category.getCategoryId(), HttpStatus.NOT_FOUND));
        }).collect(Collectors.toList()));
        blogPostRepo.save(blogpost);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "Blog Posted SuccessFully.");
        return ResponseEntity.status(successResponse.getStatus()).body(successResponse);

    }

    @Override
    public ResponseEntity getAllBlogsForUser(Long id) {
        Optional<List<BlogPost>> blogList = blogPostRepo.findByUserId(id);
        if (blogList.isPresent() && !blogList.get().isEmpty()){
            List<BlogPostDto> blogPostDtoList =  blogList.get().stream().map(blogPost -> {
                BlogPostDto blogPostDto = new BlogPostDto();
                blogPostDto.setBlogPostId(blogPost.getBlogPostId());
                blogPostDto.setTitle(blogPost.getTitle());
                blogPostDto.setContent(blogPost.getContent());
                blogPostDto.setUserId(blogPost.getUser().getId());

                blogPostDto.setTags(blogPost.getTags().stream().map(tag ->{
                    TagDto tagDto = new TagDto();
                    tagDto.setTagId(tag.getTagId());
                    tagDto.setTagName(tag.getTagName());
                    return tagDto;
                }).collect(Collectors.toList()));

                blogPostDto.setCategories(blogPost.getCategories().stream().map(category ->{
                    CategoryDto categoryDto = new CategoryDto();
                    categoryDto.setCategoryId(category.getCategoryId());
                    categoryDto.setCategoryName(category.getCategoryName());
                    return categoryDto;
                }).collect(Collectors.toList()));

                return blogPostDto;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK, "success", blogPostDtoList));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse(HttpStatus.NOT_FOUND, "no Data found For User"));

    }

    @Override
    public ResponseEntity updateBlogById(Long userId, BlogPostDto blogPostDto, Long blogPostId) {
        Optional<BlogPost> blogPostDtls = blogPostRepo.findById(blogPostId);
        if (blogPostDtls.isPresent()) {
            BlogPost blogPost = blogPostDtls.get();
            if (blogPost.getUser().getId() == userId) {
                blogPost.setContent(blogPostDto.getContent());
                blogPost.setTitle(blogPostDto.getTitle());
                //to map Tags associated with this blog-post
                blogPost.setTags(blogPostDto.getTags().stream().map(tag -> {
                    return tagRepo.findById(tag.getTagId()).orElseThrow(() -> new ApplicationException("Tag does not exist with id-> " + tag.getTagId(), HttpStatus.NOT_FOUND));
                }).collect(Collectors.toList()));
                //to map Categories associated with this blog-post
                blogPost.setCategories(blogPostDto.getCategories().stream().map(category -> {
                    return categoryRepo.findById(category.getCategoryId()).orElseThrow(() -> new ApplicationException("Category does not exist with id-> " + category.getCategoryId(), HttpStatus.NOT_FOUND));
                }).collect(Collectors.toList()));
                blogPostRepo.save(blogPost);
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK, "Updated Successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SuccessResponse(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED user cannot update this blog."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse(HttpStatus.NOT_FOUND, "no Blog found For id " + blogPostId));
        }
    }

    @Override
    public ResponseEntity deleteBlogById(Long blogPostId, Long userId) {
        Optional<BlogPost> blogPostDetails = blogPostRepo.findById(blogPostId);
        if(blogPostDetails.isPresent()){
            BlogPost blogPost = blogPostDetails.get();
            if(blogPost.getUser().getId()==userId){
                for(Tag tag : blogPostDetails.get().getTags()){
                }

                for(Category category : blogPostDetails.get().getCategories()){
                }
                blogPostRepo.deleteById(blogPostId);
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK,"Deleted Successfully"));
            }else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SuccessResponse(HttpStatus.UNAUTHORIZED, "You can not delete this blog."));
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse(HttpStatus.NOT_FOUND, "no Blog found For id " + blogPostId));
    }
}
