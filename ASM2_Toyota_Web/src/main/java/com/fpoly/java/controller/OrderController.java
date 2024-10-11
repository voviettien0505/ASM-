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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/place")
    public String placeOrder(@RequestParam List<Long> productIds,
                             @RequestParam List<Integer> quantities,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        // Tạo đơn hàng mới và lưu thông tin chi tiết đơn hàng
        orderService.createOrder(user, productIds, quantities);

        return "redirect:/order/confirmation";
    }
}

