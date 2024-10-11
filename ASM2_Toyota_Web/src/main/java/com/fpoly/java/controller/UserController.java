//package com.fpoly.java.controller;
//
//import com.fpoly.java.model.User;
//import com.fpoly.java.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//@RequestMapping("/user")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/admin")
//    public String showUserList(Model model) {
//        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("user", new User());
//        return "admin/manageUser"; // Corresponding Thymeleaf template
//    }
//
//    @PostMapping("/save")
//    public String saveUser(@ModelAttribute("user") User user) {
//        userService.saveUser(user);
//        return "redirect:/user/admin";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String editUser(@PathVariable Long id, Model model) {
//        User user = userService.getUserById(id);
//        model.addAttribute("user", user != null ? user : new User());
//        return "admin/manageUser";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return "redirect:/user/admin";
//    }
//    // Xử lý đăng ký người dùng mới
//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
//        if (userService.getUserByUsername(user.getUsername()) != null) {
//            redirectAttributes.addFlashAttribute("error", "Tên người dùng đã tồn tại.");
//            return "redirect:/user/register";
//        }
//        userService.saveUser(user);
//        return "redirect:/user/login";
//    }
//
//    // Hiển thị trang đăng nhập
//    @GetMapping("/login")
//    public String showLoginForm() {
//        return "login";
//    }
//
//    // Xử lý đăng nhập người dùng
//    @PostMapping("/login")
//    public String loginUser(@RequestParam("username") String username,
//                            @RequestParam("password") String password,
//                            RedirectAttributes redirectAttributes) {
//        User user = userService.getUserByUsername(username);
//        if (user == null || !user.getPassword().equals(password)) {
//            redirectAttributes.addFlashAttribute("error", "Sai tên người dùng hoặc mật khẩu.");
//            return "redirect:/user/login";
//        }
//        // Thêm người dùng vào phiên làm việc hoặc xử lý đăng nhập khác nếu cần
//        redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công");
//        redirectAttributes.addAttribute("id", user.getId());
//        return "redirect:/user/profile";
//    }
//
//    // Hiển thị trang cập nhật thông tin người dùng
//    @GetMapping("/profile")
//    public String showProfile(@RequestParam("id") Long userId, Model model) {
//        User user = userService.getUserById(userId);
//        model.addAttribute("user", user);
//        return "profile";
//    }
//
//    // Xử lý cập nhật thông tin người dùng
//    @PostMapping("/update")
//    public String updateUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
//        userService.saveUser(user);
//        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công");
//        return "redirect:/user/profile?id=" + user.getId();
//    }
//
//}
