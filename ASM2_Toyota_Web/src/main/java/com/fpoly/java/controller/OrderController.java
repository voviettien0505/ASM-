package com.fpoly.java.controller;

import com.fpoly.java.model.User;
import com.fpoly.java.service.OrderService;
import com.fpoly.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) {
            return "error/userNotFound";
        }

        // Create an order from the user's cart items
        orderService.createOrderFromCart(user);

        return "redirect:/order/confirmation";
    }
}
