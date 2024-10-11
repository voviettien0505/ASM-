package com.fpoly.java.service;

import com.fpoly.java.model.*;
import com.fpoly.java.repository.CartItemRepository;
import com.fpoly.java.repository.OrderDetailRepository;
import com.fpoly.java.repository.OrderRepository;
import com.fpoly.java.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    public void createOrderWithSelectedItems(User user, List<Long> productIds, List<Integer> quantities) {
        if (productIds.isEmpty() || quantities.isEmpty()) {
            throw new IllegalArgumentException("No products selected.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setDate(new Date());
        order.setStatus("Pending");

        // Save the order to generate an ID
        order = orderRepository.save(order);

        // Create order details for each selected item
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            Product product = productRepository.findById(productId).orElse(null);
            if (product != null && quantity > 0) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setPrice(product.getPrice() * quantity);

                // Save each order detail
                orderDetailRepository.save(orderDetail);
            }
        }
    }
}

