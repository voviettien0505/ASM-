package com.fpoly.java.service;

import com.fpoly.java.model.CartItem;
import com.fpoly.java.model.Product;
import com.fpoly.java.model.User;
import com.fpoly.java.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem addOrUpdateCartItem(User user, Product product, int quantity) {
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }
        return cartItemRepository.save(cartItem);
    }


    public List<CartItem> getCartItemsByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    // Inside CartItemService
    public String getProductNameByProductId(Long productId) {
        return cartItemRepository.findProductNameByProductId(productId);
    }

    public void deleteSelectedCartItems(User user, List<Long> productIds) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        List<CartItem> itemsToDelete = cartItems.stream()
                .filter(item -> productIds.contains(item.getProduct().getId()))
                .collect(Collectors.toList());

        cartItemRepository.deleteAll(itemsToDelete);
    }


}
