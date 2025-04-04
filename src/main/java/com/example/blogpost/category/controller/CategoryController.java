package com.example.blogpost.category.controller;

import com.example.blogpost.category.dto.CategoryDto;
import com.example.blogpost.category.service.CategoryService;
import com.example.blogpost.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    ResponseEntity<SuccessResponse> saveCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.saveCategory(categoryDto);
    }
    @GetMapping
    ResponseEntity findAllCategory(){
        return  categoryService.findAllCategory();
    }
}
