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
import java.text.NumberFormat;
import java.util.Locale;
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

    // Phương thức định dạng tiền tệ
    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " VND";
    }

    @GetMapping("/index")
    public String indexController(Model model) {
        List<Product> products = productService.getAllProducts();
        Map<Long, String> productImageMap = new HashMap<>();
        Map<Long, String> productPriceMap = new HashMap<>(); // Map để lưu giá định dạng

        for (Product product : products) {
            List<Image> images = imageService.getImagesByProductId(product.getId());
            if (!images.isEmpty()) {
                productImageMap.put(product.getId(), images.get(0).getImageName());
            }
            // Định dạng giá tiền và lưu vào productPriceMap
            productPriceMap.put(product.getId(), formatCurrency(product.getPrice()));
        }

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("productImageMap", productImageMap);
        model.addAttribute("productPriceMap", productPriceMap); // Gửi productPriceMap đến view
        model.addAttribute("product", new Product());

        return "user/index";
    }
}
