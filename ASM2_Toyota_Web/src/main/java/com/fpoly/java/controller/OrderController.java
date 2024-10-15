package com.fpoly.java.controller;

import com.fpoly.java.model.Order;
import com.fpoly.java.model.OrderDetail;
import com.fpoly.java.model.User;
import com.fpoly.java.service.OrderPaymentNotification;
import com.fpoly.java.service.OrderService;
import com.fpoly.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    private final SimpMessagingTemplate messagingTemplate;

    public OrderController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/sendNotification")
    @ResponseBody
    public ResponseEntity<Void> sendNotification(@RequestParam("message") String message) {
        messagingTemplate.convertAndSend("/topic/orderPayment", message);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/status")
    @ResponseBody
    public Map<String, String> getOrderStatus(@RequestParam("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        Map<String, String> response = new HashMap<>();
        if (order != null) {
            response.put("status", order.getStatus());
        } else {
            response.put("status", "NOT_FOUND");
        }
        return response;
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam List<Long> productIds,
                             @RequestParam List<Integer> quantities,
                             @RequestParam("totalAmount") double totalAmount) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) {
            return "error/userNotFound";
        }

        // Gọi phương thức và nhận đối tượng Order sau khi tạo
        Order order = orderService.createOrderWithSelectedItems(user, productIds, quantities, totalAmount);

        // Kiểm tra xem order có ID chưa
        if (order.getId() == null) {
            throw new IllegalStateException("Failed to retrieve order ID.");
        }

        return "redirect:/order/checkout?orderId=" + order.getId();
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam("orderId") Long orderId, Model model) {
        // Lấy thông tin đơn hàng và chi tiết đơn hàng
        Order order = orderService.getOrderById(orderId);
        List<OrderDetail> orderDetails = orderService.getOrderDetailsByOrderId(orderId);
        // Thêm thông tin vào model để hiển thị trên trang
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        return "user/bayment";
    }

    // Hiển thị danh sách đơn hàng và form thêm/sửa
    @GetMapping("/admin")
    public String showOrderList(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("order", new Order()); // Đối tượng Order mới để thêm
        return "admin/manageOrder";
    }

//    // Thêm hoặc cập nhật đơn hàng
//    @PostMapping("/save")
//    public String saveOrder(@ModelAttribute("order") Order order) {
//        orderService.saveOrUpdateOrder(order);
//        return "redirect:/order/admin";
//    }


    // Chỉnh sửa đơn hàng
    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("order", order != null ? order : new Order());
        return "admin/manageOrder";
    }

    // Xóa đơn hàng
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/order/admin";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute("order") Order order) {
        orderService.saveOrUpdateOrder(order);
        // Kiểm tra nếu trạng thái đơn hàng là "DA_THANH_TOAN"
        if ("DA_THANH_TOAN".equals(order.getStatus())) {
            // Gọi phương thức cập nhật số lượng sản phẩm
            orderService.updateProductQuantitiesForOrder(order.getId());
        }
        return "redirect:/order/admin";
    }

}


