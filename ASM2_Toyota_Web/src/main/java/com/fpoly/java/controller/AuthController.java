package com.fpoly.java.controller;

import com.fpoly.java.model.User;
import com.fpoly.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "login/register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login/login";
    }

    @GetMapping("/user/update")
    public String showUpdateForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "login/user-update";
    }

    @PostMapping("/user/update")
    public String updateUser(User user, @AuthenticationPrincipal UserDetails userDetails) {
        User existingUser = userService.getUserByUsername(userDetails.getUsername());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        if (!user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.saveUser(existingUser);
        return "redirect:/home/index";
    }
    @GetMapping("/redirectAfterLogin")
    public String redirectAfterLogin(@AuthenticationPrincipal UserDetails userDetails) {
        // Lấy thông tin người dùng từ dịch vụ
        User user = userService.getUserByUsername(userDetails.getUsername());

        // Kiểm tra nếu vai trò là ADMIN, chuyển hướng đến trang quản lý người dùng
        if (user.getRole().equals("ADMIN")) {
            return "redirect:/user/admin";
        }

        // Nếu không phải ADMIN, chuyển hướng về trang chủ người dùng
        return "redirect:/home/index";
    }
}
