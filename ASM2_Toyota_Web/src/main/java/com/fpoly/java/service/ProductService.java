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

    // Cập nhật số lượng sản phẩm
    public Product updateProductQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setQuantity(quantity);  // Giả sử Product có trường quantity
            return productRepository.save(product);
        } else {
            return null; // Hoặc có thể ném ra một ngoại lệ
        }
    }
    public int getProductQuantityById(Long productId) {
        // Tìm sản phẩm theo ID trong cơ sở dữ liệu
        return productRepository.findById(productId)
                .map(Product::getQuantity)
                .orElse(0); // Trả về 0 nếu không tìm thấy sản phẩm
    }
}
