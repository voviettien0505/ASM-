package com.fpoly.java.controller;

import com.fpoly.java.model.Category;
import com.fpoly.java.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/admin")
    public String showCategoryList(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());
        return "admin/manageCategory";
    }
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category, @RequestPart(value = "image", required = false) MultipartFile image) {

        if (image != null && !image.isEmpty()) {
            try {
                Path uploadDir = Paths.get("images");
                Files.createDirectories(uploadDir);


                String fileName = new Date().getTime() + "_" + image.getOriginalFilename();
                Files.copy(image.getInputStream(), uploadDir.resolve(fileName));
                category.setCategoryImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (category.getId() != null) {

            Category existingCategory = categoryService.getCategoryById(category.getId());
            if (existingCategory != null) {
                category.setCategoryImage(existingCategory.getCategoryImage());
            }
        }

        categoryService.saveCategory(category);
        return "redirect:/category/admin";
    }



    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
        } else {
            model.addAttribute("category", new Category());
        }
        return "admin/manageCategory";
    }


    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/category/admin";
    }

    @GetMapping("/add")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin/manageCategory";
    }
}
