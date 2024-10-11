package com.fpoly.java.service;

import com.fpoly.java.model.Category;
import com.fpoly.java.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category) {
        // Tìm kiếm category hiện có
        Category existingCategory = getCategoryById(category.getId());
        if (existingCategory != null) {
            // Cập nhật thông tin mới
            existingCategory.setCategoryName(category.getCategoryName());
            existingCategory.setDescription(category.getDescription());

            // Cập nhật hình ảnh nếu có
            if (category.getCategoryImage() != null) {
                existingCategory.setCategoryImage(category.getCategoryImage());
            }
            return categoryRepository.save(existingCategory);
        }
        return null;
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
