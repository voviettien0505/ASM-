    package com.fpoly.java.model;

    import jakarta.persistence.*;
    import lombok.Data;
    import java.util.List;

    @Entity
    @Table(name = "products")
    @Data
    public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String description;
        private String name;
        private double price;
        private int quantity;


        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category category;

        @OneToMany(mappedBy = "product")
        private List<OrderDetail> orderDetails;

        @OneToMany(mappedBy = "product")
        private List<CartItem> cartItems;


        @OneToMany(mappedBy = "product")
        private List<Image> imageNames;


    }
