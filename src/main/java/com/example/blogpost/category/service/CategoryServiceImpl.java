package com.example.blogpost.category.service;

import com.example.blogpost.category.dto.CategoryDto;
import com.example.blogpost.category.model.Category;
import com.example.blogpost.category.repo.CategoryRepo;
import com.example.blogpost.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public ResponseEntity<SuccessResponse> saveCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        categoryRepo.save(category);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "Category added SuccessFully.");
        return  ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }
    @Override
    public ResponseEntity findAllCategory(){
        List<Category> categoryList = categoryRepo.findAll();
        if(!categoryList.isEmpty()){
            List<CategoryDto> categoryDtos = categoryList.stream().map(category->{
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setCategoryId(category.getCategoryId());
                categoryDto.setCategoryName(category.getCategoryName());
                return categoryDto;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK,categoryDtos));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK,"No Categorys Found."));
        }
    }
}
