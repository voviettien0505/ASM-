package com.fpoly.java.service;

import com.fpoly.java.model.*;
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

    @Autowired
    private CartItemService cartItemService;

    public Order createOrderWithSelectedItems(User user, List<Long> productIds, List<Integer> quantities, double totalAmount) {
        if (productIds.isEmpty() || quantities.isEmpty()) {
            throw new IllegalArgumentException("No products selected.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setDate(new Date());
        order.setStatus("Pending");
        order.setTotalAmount(totalAmount);  // Set total amount here

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

        // Delete selected items from the user's cart
        cartItemService.deleteSelectedCartItems(user, productIds);
        return order;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
}
