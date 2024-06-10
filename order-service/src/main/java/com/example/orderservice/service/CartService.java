package com.example.orderservice.service;

import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.entities.Cart;
import com.example.orderservice.entities.Product;
import com.example.orderservice.repository.CartRepository;
import com.example.orderservice.DTO.OrderDTO;
import com.example.orderservice.DTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private ProductClient productClient;

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addProductToCart(String userId, String productId, int quantity) {
        Cart cart = getCartByUserId(userId).orElse(new Cart(userId));

        // Fetch product details from ProductService
        ProductClient.ProductResponse productDTO = productClient.getProductById(productId);
        if (productDTO == null) {
            throw new RuntimeException("Product not found in ProductService");
        }

        Optional<Product> existingProduct = cart.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst();

        if (existingProduct.isPresent()) {
            Product prod = existingProduct.get();
            prod.setQuantity(prod.getQuantity() + quantity);
            prod.setTotalPrice(productDTO.getPrice().multiply(BigDecimal.valueOf(prod.getQuantity() ))); // Use getPrice directly
        } else {
            Product product = new Product();
            product.setId(productDTO.getId());
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice()); // Use getPrice directly
            product.setQuantity(quantity);
            product.setTotalPrice(productDTO.getPrice().multiply(BigDecimal.valueOf(quantity ))); // Use getPrice directly
            product.setIdChef(productDTO.getIdChef()); // Assuming ProductDTO contains ChefId
            product.setImageUrl(productDTO.getImageUrl());

            cart.addProduct(product);
        }

        saveCart(cart);
    }

    public void updateProductInCart(String userId, String productId, int quantity) throws Exception {
        Optional<Cart> optionalCart = getCartByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            Optional<Product> existingProduct = cart.getProducts().stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst();

            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();
                product.setQuantity(quantity);
                product.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
                saveCart(cart);
            } else {
                throw new Exception("Product not found in the cart");
            }
        } else {
            throw new Exception("Cart not found for user " + userId);
        }
    }

    public void removeProductFromCart(String userId, String productId) {
        Optional<Cart> optionalCart = getCartByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.removeProduct(productId);
            saveCart(cart);
        }
    }

    public void deleteCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }


    //notif

    public void checkout(String userId, String adresse) {
        Optional<Cart> optionalCart = getCartByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            // Separate products by chef
            Map<String, List<ProductDTO>> productsByChef = cart.getProducts().stream()
                    .collect(Collectors.groupingBy(Product::getIdChef,
                            Collectors.mapping(product -> new ProductDTO(
                                    product.getId(),
                                    product.getName(),
                                    product.getPrice(),
                                    product.getQuantity(),
                                    product.getTotalPrice(),
                                    product.getIdChef(),
                                    product.getImageUrl()
                            ), Collectors.toList())));

            // For each chef, create individual orders
            productsByChef.forEach((chefId, productDTOs) -> {
                OrderDTO order = new OrderDTO();
                order.setUserId(userId);
                order.setProducts(productDTOs);
                order.setTotalPrice(productDTOs.stream()
                        .map(ProductDTO::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                order.setGrouped(false); // Indicates individual order
                order.setChefId(chefId); // Set chefId
                order.setStatusChef("invalide");
                order.setAdresse(adresse);
                paymentClient.createOrder(order);
            });

            // Check if there is an existing grouped order for this user
            ResponseEntity<List<OrderDTO>> response = paymentClient.getGroupedOrdersByUserId(userId);
            List<OrderDTO> existingGroupedOrders = response.getBody();

            OrderDTO groupedOrder;
            if (existingGroupedOrders != null && !existingGroupedOrders.isEmpty()) {
                // Update the existing grouped order
                groupedOrder = existingGroupedOrders.get(0);
                List<ProductDTO> updatedProducts = groupedOrder.getProducts();
                updatedProducts.addAll(cart.getProducts().stream()
                        .map(product -> new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getQuantity(),
                                product.getTotalPrice(),
                                product.getIdChef(),
                                product.getImageUrl()
                        )).collect(Collectors.toList()));
                groupedOrder.setProducts(updatedProducts);
                groupedOrder.setTotalPrice(updatedProducts.stream()
                        .map(ProductDTO::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

                paymentClient.updateOrder(groupedOrder.getId(), groupedOrder);
            } else {
                // Create a new grouped order
                groupedOrder = new OrderDTO();
                groupedOrder.setUserId(userId);
                groupedOrder.setProducts(cart.getProducts().stream()
                        .map(product -> new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getQuantity(),
                                product.getTotalPrice(),
                                product.getIdChef(),
                                product.getImageUrl()
                        )).collect(Collectors.toList()));
                groupedOrder.setTotalPrice(cart.getProducts().stream()
                        .map(Product::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                groupedOrder.setGrouped(true); // Indicates grouped order
                groupedOrder.setChefId("grouped"); // Use "grouped" for grouped orders
                groupedOrder.setAdresse(adresse);
                paymentClient.createOrder(groupedOrder);
            }

            // Delete the cart after checkout
            deleteCart(userId);
        }
    }


    public void removeProductFromAllCarts(String productId) {
        // Retrieve all carts from the repository
        List<Cart> allCarts = cartRepository.findAll();

        for (Cart cart : allCarts) {
            // Remove the product from the cart
            cart.removeProduct(productId);

            // Update the total price of the cart
            cart.updateTotalPrice();

            // Save the updated cart back to the repository
            cartRepository.save(cart);
        }
    }
}

