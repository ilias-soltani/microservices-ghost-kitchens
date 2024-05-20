package com.esisba.orderservice.service;

import java.util.ArrayList;
import java.util.Optional;

import com.esisba.orderservice.Repository.CartRepository;
import com.esisba.orderservice.Repository.OrderRepository;
import com.esisba.orderservice.entities.Cart;
import com.esisba.orderservice.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartById(String id) {
        return cartRepository.findById(id);
    }

    public Optional<Cart> getActiveCartByUserId(String userId) {
        return cartRepository.findByUserIdAndPaid(userId, false);
    }

    public void deleteCart(String id) {
        cartRepository.deleteById(id);
    }

    public Cart addToCart(Order order) {
        Optional<Cart> optionalCart = getActiveCartByUserId(order.getUser().getId());

        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            cart = new Cart();
            cart.setUser(order.getUser());
            cart.setPaid(false);
            cart.setProducts(new ArrayList<>()); // Initialize the products list
        }

        if (cart.getProducts() == null) {
            cart.setProducts(new ArrayList<>()); // Initialize if not already initialized
        }

        cart.getProducts().addAll(order.getProducts());
        return saveCart(cart);
    }

    public void markCartAsPaid(String cartId) {
        Optional<Cart> optionalCart = getCartById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.setPaid(true);
            saveCart(cart);
        }
    }
}
