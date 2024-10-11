package com.fpoly.java.controller;

import com.fpoly.java.model.Image;
import com.fpoly.java.model.Product;
import com.fpoly.java.service.ImageService;
import com.fpoly.java.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductService productService;

    // Lấy danh sách hình ảnh của sản phẩm
    @GetMapping("/list/{productId}")
    @ResponseBody
    public List<Image> getImagesByProductId(@PathVariable Long productId) {
        return imageService.getImagesByProductId(productId);
    }

    @PostMapping("/add")
    public String addImages(@RequestParam("productId") Long productId,
                            @RequestParam("files") List<MultipartFile> files) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        Path uploadDir = Paths.get("images");
                        Files.createDirectories(uploadDir);

                        // Tạo tên tệp duy nhất và lưu ảnh
                        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        Files.copy(file.getInputStream(), uploadDir.resolve(fileName));

                        // Lưu thông tin hình ảnh vào cơ sở dữ liệu
                        Image image = new Image();
                        image.setImageName(fileName);
                        image.setProduct(product);
                        imageService.saveImage(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "redirect:/product/manage-images/" + productId + "?error=Failed to save images";
                    }
                }
            }
        }
        return "redirect:/product/manage-images/" + productId;
    }

    // Sử dụng @GetMapping để hỗ trợ xóa qua GET request
    @GetMapping("/delete/{imageId}/{productId}")
    public String deleteImage(@PathVariable Long imageId, @PathVariable Long productId) {
        imageService.deleteImage(imageId);  // Xóa ảnh theo ID
        // Kiểm tra và chuyển hướng lại trang quản lý hình ảnh của sản phẩm
        return "redirect:/product/manage-images/" + productId;
    }
}
