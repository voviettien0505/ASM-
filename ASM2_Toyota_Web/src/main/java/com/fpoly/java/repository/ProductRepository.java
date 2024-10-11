package com.fpoly.java.repository;

import com.fpoly.java.model.Product;
import com.fpoly.java.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
