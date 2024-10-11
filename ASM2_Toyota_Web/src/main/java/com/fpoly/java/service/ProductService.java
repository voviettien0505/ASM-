package com.fpoly.java.service;

import com.fpoly.java.model.Product;
import com.fpoly.java.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    // Chỉ lưu sản phẩm mà không lưu hình ảnh
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {

        productRepository.deleteById(productId);
    }

    public String getProductNameByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        return product != null ? product.getName() : "Product not found";
    }

}