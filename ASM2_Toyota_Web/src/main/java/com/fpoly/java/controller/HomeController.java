package com.fpoly.java.controller;


import com.fpoly.java.model.Category;
import com.fpoly.java.model.Image;
import com.fpoly.java.model.Product;
import com.fpoly.java.service.CategoryService;
import com.fpoly.java.service.ImageService;
import com.fpoly.java.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    public ProductService productService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/index")
    public String indexController(Model model) {
        List<Product> products = productService.getAllProducts();

        // Map lưu sản phẩm ID và tên ảnh đầu tiên
        Map<Long, String> productImageMap = new HashMap<>();

        // Lặp qua từng sản phẩm để lấy ảnh đầu tiên
        for (Product product : products) {
            List<Image> images = imageService.getImagesByProductId(product.getId());
            if (!images.isEmpty()) {
                productImageMap.put(product.getId(), images.get(0).getImageName());
            }
        }
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("productImageMap", productImageMap);
        model.addAttribute("product", new Product());

        return "user/index";

    }
}
