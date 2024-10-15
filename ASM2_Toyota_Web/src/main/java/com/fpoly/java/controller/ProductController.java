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
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        if (product.getId() != null) {
            Product existingProduct = productService.getProductById(product.getId());
            if (existingProduct != null) {

                product.setCategory(existingProduct.getCategory());
            }
        }
        productService.saveProduct(product);
        return "redirect:/product/admin";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Category> categories = categoryService.getAllCategories();

        if (product != null) {
            // Định dạng giá tiền và truyền vào view
            String formattedPrice = formatCurrency(product.getPrice());
            model.addAttribute("formattedPrice", formattedPrice);
            model.addAttribute("product", product);
        } else {
            model.addAttribute("product", new Product());
        }

        model.addAttribute("categories", categories);
        return "admin/manageProduct";
    }


    // Xóa sản phẩm theo ID
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/product/admin";
    }

    // Thêm mới sản phẩm
    @GetMapping("/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return "admin/manageProduct";
    }

    @GetMapping("/manage-images/{productId}")
    public String manageImages(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);
        List<Image> images = imageService.getImagesByProductId(productId);
        model.addAttribute("product", product);
        model.addAttribute("images", images);
        return "admin/manageImages";
    }

    // Phương thức định dạng tiền tệ
    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " VND";
    }

    // Hiển thị danh sách sản phẩm
    @GetMapping("/admin")
    public String showProductList(Model model) {
        List<Product> products = productService.getAllProducts();

        // Map lưu sản phẩm ID và tên ảnh đầu tiên
        Map<Long, String> productImageMap = new HashMap<>();
        Map<Long, String> productPriceMap = new HashMap<>(); // Map để lưu giá định dạng
        // Lặp qua từng sản phẩm để lấy ảnh đầu tiên
        for (Product product : products) {
            List<Image> images = imageService.getImagesByProductId(product.getId());
            if (!images.isEmpty()) {
                productImageMap.put(product.getId(), images.get(0).getImageName());
            }
            productPriceMap.put(product.getId(), formatCurrency(product.getPrice()));
        }
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("productImageMap", productImageMap);
        model.addAttribute("productPriceMap", productPriceMap); // Gửi productPriceMap đến view
        model.addAttribute("product", new Product());

        return "admin/manageProduct";
    }

    // Hiển thị chi tiết sản phẩm
    @GetMapping("/detail/{productId}")
    public String showProductDetail(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);
        List<Image> images = imageService.getImagesByProductId(productId);

        // Định dạng giá tiền của sản phẩm
        String formattedPrice = formatCurrency(product.getPrice());

        model.addAttribute("product", product);
        model.addAttribute("images", images);
        model.addAttribute("formattedPrice", formattedPrice); // Truyền giá đã định dạng đến view
        return "user/product-detail";
    }
    @GetMapping("/quantity")
    public Map<String, Integer> getProductQuantityById(@RequestParam Long productId) {
        int quantity = productService.getProductQuantityById(productId);
        Map<String, Integer> response = new HashMap<>();
        response.put("quantity", quantity);
        return response;
    }

}
