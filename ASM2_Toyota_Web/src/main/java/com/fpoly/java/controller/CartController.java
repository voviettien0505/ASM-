package com.fpoly.java.controller;

import com.fpoly.java.model.CartItem;
import com.fpoly.java.model.Product;
import com.fpoly.java.model.User;
import com.fpoly.java.service.CartItemService;
import com.fpoly.java.service.ProductService;
import com.fpoly.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // View Cart
    @GetMapping("/view")
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            // Redirect to login if the user is not authenticated
            return "redirect:/login";
        }

        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            // Handle the case if user is not found
            return "error/userNotFound";
        }

        // Get cart items for the authenticated user
        List<CartItem> cartItems = cartItemService.getCartItemsByUser(user);

        // Add attributes to the model for display
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);

        return "cart/view";
    }

    // Add to Cart
    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam Long productId, @RequestParam int quantity) {
        if (userDetails == null) {
            return "User is not authenticated";
        }

        User user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) {
            return "User not found";
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return "Product not found";
        }

        if (quantity <= 0) {
            return "Invalid quantity";
        }

        if (quantity > product.getQuantity()) {
            return "Out of stock";
        }

        // Add or update the cart item for the authenticated user
        cartItemService.addOrUpdateCartItem(user, product, quantity);
        return "Product added to cart";
    }

    // Remove from Cart
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
        return "redirect:/cart/view";
    }
}
