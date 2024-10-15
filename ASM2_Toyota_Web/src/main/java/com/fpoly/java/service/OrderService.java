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
import java.util.Optional;

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


    @Autowired
    private ProductService productService;

    public void updateProductQuantitiesForOrder(Long orderId) {
        // Lấy danh sách chi tiết đơn hàng
        List<OrderDetail> orderDetails = getOrderDetailsByOrderId(orderId);
        for (OrderDetail detail : orderDetails) {
            Product product = detail.getProduct();
            int newQuantity = product.getQuantity() - detail.getQuantity();
            // Đảm bảo số lượng sản phẩm không âm
            if (newQuantity >= 0) {
                product.setQuantity(newQuantity);
                productRepository.save(product); // Cập nhật sản phẩm vào kho
            } else {
                throw new IllegalArgumentException("Số lượng sản phẩm không đủ trong kho.");
            }
        }
    }


    public Order createOrderWithSelectedItems(User user, List<Long> productIds, List<Integer> quantities, double totalAmount) {
        if (productIds.isEmpty() || quantities.isEmpty()) {
            throw new IllegalArgumentException("No products selected.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setDate(new Date());
        order.setStatus("CHO_XAC_NHAN");
        order.setTotalAmount(totalAmount);  // Set total amount here
        order.setAddress(user.getAddress());
        order.setPhone(user.getPhone());
        order.setFullName(user.getFullName());

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
    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }


    // Lấy danh sách tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Lấy thông tin chi tiết đơn hàng theo ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // Lưu hoặc cập nhật đơn hàng
    public Order saveOrUpdateOrder(Order order) {
        return orderRepository.save(order);
    }

    // Xóa đơn hàng
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    // Cập nhật một đơn hàng cụ thể
    public Order updateOrder(Long orderId, Order updatedOrder) {
        Optional<Order> existingOrderOpt = orderRepository.findById(orderId);
        if (existingOrderOpt.isPresent()) {
            Order existingOrder = existingOrderOpt.get();
            existingOrder.setAddress(updatedOrder.getAddress());
            existingOrder.setDate(updatedOrder.getDate());
            existingOrder.setFullName(updatedOrder.getFullName());
            existingOrder.setPhone(updatedOrder.getPhone());
            existingOrder.setStatus(updatedOrder.getStatus());
            existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
            existingOrder.setUser(updatedOrder.getUser());
            return orderRepository.save(existingOrder);
        } else {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
    }



}
