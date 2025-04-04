package com.example.blogpost.category.service;

import com.example.blogpost.category.dto.CategoryDto;
import com.example.blogpost.response.SuccessResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<SuccessResponse> saveCategory(CategoryDto categoryDto);
    ResponseEntity findAllCategory();
}
