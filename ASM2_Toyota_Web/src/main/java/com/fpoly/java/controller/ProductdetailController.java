package com.fpoly.java.controller;

import com.fpoly.java.model.Image;
import com.fpoly.java.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ProductdetailController {
    @Autowired
    ProductService productService;
//    // Lấy danh sách hình ảnh của sản phẩm
//    @GetMapping("/list/{productId}")
//    @ResponseBody
//    public List<Image> getImagesByProductId(@PathVariable Long productId) {
//        return ProductService.;
//    }
}
