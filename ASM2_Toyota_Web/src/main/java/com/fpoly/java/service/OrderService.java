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

    @Autowired
    private CartItemService cartItemService;

    public void createOrderFromCart(User user) {
        List<CartItem> cartItems = cartItemService.getCartItemsByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setDate(new Date());
        order.setStatus("Pending");

        // Save the order first to generate an order ID
        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

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

        // Clear the user's cart after placing the order
//        cartItemService.clearCartItemsByUser(user);
    }
}
