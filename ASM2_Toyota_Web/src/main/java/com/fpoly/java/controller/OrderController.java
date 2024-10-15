package com.fpoly.java.controller;

import com.fpoly.java.model.Order;
import com.fpoly.java.model.User;
import com.fpoly.java.service.OrderService;
import com.fpoly.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam List<Long> productIds,
                             @RequestParam List<Integer> quantities,
                             @RequestParam("totalAmount") double totalAmount,
                             Model model) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) {
            return "error/userNotFound";
        }

        // Create the order and get the created order
        Order newOrder = orderService.createOrderWithSelectedItems(user, productIds, quantities, totalAmount);

        // Add the orderId to the model
        model.addAttribute("orderId", newOrder.getId());

        // Redirect to the confirmation page with the orderId
        return "redirect:/order/confirmation?orderId=" + newOrder.getId();
    }

    @GetMapping("/confirmation")
    public String showConfirmation(@RequestParam("orderId") Long orderId, Model model) {
        // Fetch the order details using the orderId (optional, if needed for confirmation details)
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "error/orderNotFound";
        }

        // Add the order to the model for display in the confirmation page
        model.addAttribute("order", order);

        return "order/confirmation";
    }

    // Retrieve and display orders for the logged-in user
    @GetMapping("/user/orders")
    public String getUserOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) {
            return "error/userNotFound";
        }

        List<Order> userOrders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute("orders", userOrders);

        return "user/orderUser"; // Return view to display orders for the user
    }
}
