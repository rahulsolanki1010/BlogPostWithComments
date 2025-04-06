package com.example.blogpost.blogPost.service;

import com.example.blogpost.blogPost.dto.BlogPostDto;
import com.example.blogpost.blogPost.model.BlogPost;
import com.example.blogpost.blogPost.repo.BlogPostRepo;
import com.example.blogpost.category.dto.CategoryDto;
import com.example.blogpost.category.model.Category;
import com.example.blogpost.category.repo.CategoryRepo;
import com.example.blogpost.comment.dto.CommentDto;
import com.example.blogpost.comment.repo.CommentRepo;
import com.example.blogpost.exception.ApplicationException;
import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.tag.dto.TagDto;
import com.example.blogpost.tag.model.Tag;
import com.example.blogpost.tag.repo.TagRepo;
import com.example.blogpost.user.dto.UserDTO;
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
    @Autowired
    CommentRepo commentRepo;

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

                blogPostDto.setComments(commentRepo.findByBlogPostBlogPostId(blogPost.getBlogPostId()).get().stream().map(comments -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setCommentText(comments.getCommentText());
                    commentDto.setCommentId(comments.getCommentId());
                    commentDto.setParentCommentId(comments.getParentCommentId());
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUsername(comments.getUser().getUsername());
                    userDTO.setId(comments.getUser().getId());
                    commentDto.setUserDTO(userDTO);
                    return commentDto;
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

    @Override
    public ResponseEntity getAllBlogs() {
        List<BlogPost> lstBlogs = blogPostRepo.findAll();
        if (lstBlogs != null && !lstBlogs.isEmpty()) {
            List<BlogPostDto> lstBlogPostMst = lstBlogs.stream().map(blogpost -> {
                BlogPostDto blogPostDto = new BlogPostDto();
                blogPostDto.setUserId(blogpost.getUser().getId());
                blogPostDto.setBlogPostId(blogpost.getBlogPostId());
                blogPostDto.setTitle(blogpost.getTitle());
                blogPostDto.setContent(blogpost.getContent());

                //to map Tags associated with this blog-post
                blogPostDto.setTags(blogpost.getTags().stream().map(tag -> {
                    Tag childTag = tagRepo.findById(tag.getTagId()).orElseThrow(() -> new ApplicationException("Tag does not exist with id-> " + tag.getTagId(), HttpStatus.NOT_FOUND));
                    TagDto tagDto = new TagDto();
                    tagDto.setTagId(childTag.getTagId());
                    tagDto.setTagName(childTag.getTagName());
                    return tagDto;
                }).collect(Collectors.toList()));

                //to map Categories associated with this blog-post
                blogPostDto.setCategories(blogpost.getCategories().stream().map(category -> {
                    Category childCategory = categoryRepo.findById(category.getCategoryId()).orElseThrow(() -> new ApplicationException("Category does not exist with id-> " + category.getCategoryId(), HttpStatus.NOT_FOUND));
                    CategoryDto categoryDto = new CategoryDto();
                    categoryDto.setCategoryId(childCategory.getCategoryId());
                    categoryDto.setCategoryName(childCategory.getCategoryName());
                    return categoryDto;
                }).collect(Collectors.toList()));

                //to map Comments associated with this blog-post
                blogPostDto.setComments(commentRepo.findAllByBlogPostBlogPostId(blogpost.getBlogPostId()).get().stream().map(comment ->
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
                ).collect(Collectors.toList()));
                return blogPostDto;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK, "success", lstBlogPostMst));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse(HttpStatus.NOT_FOUND, "Data found null"));
    }

    @Override
    public ResponseEntity getAllBlogsByTags(String tagName) {
        Tag tag = tagRepo.findByTagNameIgnoreCase(tagName);
        List<BlogPost> lstBlogs = blogPostRepo.findAllByTagsTagId(tag.getTagId());
        if (lstBlogs != null && !lstBlogs.isEmpty()) {
            List<BlogPostDto> lstBlogPostMst = lstBlogs.stream().map(blogpost -> {
                BlogPostDto blogPostMst = new BlogPostDto();
                blogPostMst.setUserId(blogpost.getUser().getId());
                blogPostMst.setBlogPostId(blogpost.getBlogPostId());
                blogPostMst.setTitle(blogpost.getTitle());
                blogPostMst.setContent(blogpost.getContent());

                //to map Tags associated with this blog-post
                blogPostMst.setTags(blogpost.getTags().stream().map(tags -> {
                    Tag childTag = tagRepo.findById(tags.getTagId()).orElseThrow(() -> new ApplicationException("Tag does not exist with id-> " + tags.getTagId(), HttpStatus.NOT_FOUND));
                    TagDto tagDto = new TagDto();
                    tagDto.setTagId(childTag.getTagId());
                    tagDto.setTagName(childTag.getTagName());
                    return tagDto;
                }).collect(Collectors.toList()));

                //to map Categories associated with this blog-post
                blogPostMst.setCategories(blogpost.getCategories().stream().map(category -> {
                    Category childCategory = categoryRepo.findById(category.getCategoryId()).orElseThrow(() -> new ApplicationException("Category does not exist with id-> " + category.getCategoryId(), HttpStatus.NOT_FOUND));
                    CategoryDto categoryDto = new CategoryDto();
                    categoryDto.setCategoryId(childCategory.getCategoryId());
                    categoryDto.setCategoryName(childCategory.getCategoryName());
                    return categoryDto;
                }).collect(Collectors.toList()));

                //to map Comments associated with this blog-post
                blogPostMst.setComments(commentRepo.findAllByBlogPostBlogPostId(blogpost.getBlogPostId()).get().stream().map(comment ->
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
                ).collect(Collectors.toList()));
                return blogPostMst;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK, "success", lstBlogPostMst));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse(HttpStatus.NOT_FOUND, "Data found null"));
    }

    @Override
    public ResponseEntity getAllBlogsByCategories(String categoryName){
        Category category = categoryRepo.findByCategoryNameIgnoreCase(categoryName);
        List<BlogPost> lstBlogs = blogPostRepo.findAllByCategoriesCategoryId(category.getCategoryId());
        if (lstBlogs != null && !lstBlogs.isEmpty()) {
            List<BlogPostDto> lstBlogPostMst = lstBlogs.stream().map(blogpost -> {
                BlogPostDto blogPostDto= new BlogPostDto();
                blogPostDto.setUserId(blogpost.getUser().getId());
                blogPostDto.setBlogPostId(blogpost.getBlogPostId());
                blogPostDto.setTitle(blogpost.getTitle());
                blogPostDto.setContent(blogpost.getContent());

                //to map Tags associated with this blog-post
                blogPostDto.setTags(blogpost.getTags().stream().map(tags -> {
                    Tag childTag = tagRepo.findById(tags.getTagId()).orElseThrow(() -> new ApplicationException("Tag does not exist with id-> " + tags.getTagId(), HttpStatus.NOT_FOUND));
                    TagDto tagDto = new TagDto();
                    tagDto.setTagId(childTag.getTagId());
                    tagDto.setTagName(childTag.getTagName());
                    return tagDto;
                }).collect(Collectors.toList()));

                //to map Categories associated with this blog-post
                blogPostDto.setCategories(blogpost.getCategories().stream().map(categories -> {
                    Category childCategory = categoryRepo.findById(categories.getCategoryId()).orElseThrow(() -> new ApplicationException("Category does not exist with id-> " + categories.getCategoryId(), HttpStatus.NOT_FOUND));
                    CategoryDto categoryDto = new CategoryDto();
                    categoryDto.setCategoryId(childCategory.getCategoryId());
                    categoryDto.setCategoryName(childCategory.getCategoryName());
                    return categoryDto;
                }).collect(Collectors.toList()));

                //to map Comments associated with this blog-post
                blogPostDto.setComments(commentRepo.findAllByBlogPostBlogPostId(blogpost.getBlogPostId()).get().stream().map(comment ->
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
                ).collect(Collectors.toList()));
                return blogPostDto;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK, "success", lstBlogPostMst));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse(HttpStatus.NOT_FOUND, "Data found null"));
    }
}
