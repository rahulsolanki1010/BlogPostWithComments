package com.example.blogpost.category.repo;

import com.example.blogpost.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    Category findByCategoryNameIgnoreCase(String categoryName);
}
