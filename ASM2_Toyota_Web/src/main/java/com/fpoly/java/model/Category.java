package com.fpoly.java.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryImage;
    private String description;
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    // Getters and Setters
}
