package com.fpoly.java.repository;

import com.fpoly.java.model.CartItem;
import com.fpoly.java.model.Product;
import com.fpoly.java.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndProduct(User user, Product product);
    List<CartItem> findByUser(User user);

    // Inside CartItemRepository
    @Query("SELECT p.name FROM CartItem ci JOIN ci.product p WHERE ci.product.id = :productId")
    String findProductNameByProductId(@Param("productId") Long productId);

    // Existing methods...

    @Query("SELECT ci.quantity FROM CartItem ci WHERE ci.product.id = :productId")
    int findQuantityByProductId(@Param("productId") Long productId);
}
