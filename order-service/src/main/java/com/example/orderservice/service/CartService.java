package com.example.orderservice.service;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import com.example.orderservice.entities.Cart;
import com.example.orderservice.entities.Product;
import com.example.orderservice.repository.CartRepository;
import com.example.paymentservice.DTO.ProductDTO;
import com.example.paymentservice.DTO.OrderDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addProductToCart(String userId, Product product) {
        Cart cart = getCartByUserId(userId).orElse(new Cart(userId));

        Optional<Product> existingProduct = cart.getProducts().stream()
                .filter(p -> p.getId().equals(product.getId()))
                .findFirst();

        if (existingProduct.isPresent()) {
            Product prod = existingProduct.get();
            prod.setQuantity(prod.getQuantity() + product.getQuantity());
            prod.setTotalPrice(prod.getQuantity() * prod.getUnitPrice());
        } else {
            product.setTotalPrice(product.getQuantity() * product.getUnitPrice());
            cart.addProduct(product);
        }

        saveCart(cart);
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

    public void updateProductInCart(String userId, Product product) throws Exception {
        Optional<Cart> optionalCart = getCartByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            Optional<Product> existingProduct = cart.getProducts().stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst();

            if (existingProduct.isPresent()) {
                cart.updateProduct(product);
                saveCart(cart);
            } else {
                throw new Exception("Product not found in the cart");
            }
        } else {
            throw new Exception("Cart not found for user " + userId);
        }
    }

    public void checkout(String userId) {
        Optional<Cart> optionalCart = getCartByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            String orderServiceUrl = "http://localhost:8081/api/orders";

            // Séparer les produits par chef
            Map<String, List<ProductDTO>> productsByChef = cart.getProducts().stream()
                    .collect(Collectors.groupingBy(Product::getChefId,
                            Collectors.mapping(product -> new ProductDTO(
                                    product.getId(),
                                    product.getName(),
                                    product.getUnitPrice(),
                                    product.getQuantity(),
                                    product.getTotalPrice(),
                                    product.getChefId()
                            ), Collectors.toList())));

            // Pour chaque chef, créer des commandes individuelles
            productsByChef.forEach((chefId, productDTOs) -> {
                OrderDTO order = new OrderDTO();
                order.setUserId(userId);
                order.setProducts(productDTOs);
                order.setTotalPrice(productDTOs.stream().mapToDouble(ProductDTO::getTotalPrice).sum());
                order.setGrouped(false); // Indique qu'il s'agit d'une commande individuelle
                order.setChefId(chefId); // Ajoutez cette ligne
                restTemplate.postForObject(orderServiceUrl, order, OrderDTO.class);
            });

            // Vérifier s'il existe déjà une commande groupée pour cet utilisateur
            ResponseEntity<List<OrderDTO>> response = restTemplate.exchange(
                    orderServiceUrl + "/user/" + userId + "/grouped",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<OrderDTO>>() {}
            );

            List<OrderDTO> existingGroupedOrders = response.getBody();

            OrderDTO groupedOrder;
            if (existingGroupedOrders != null && !existingGroupedOrders.isEmpty()) {
                // Mettre à jour l'ordre groupé existant
                groupedOrder = existingGroupedOrders.get(0);
                List<ProductDTO> updatedProducts = groupedOrder.getProducts();
                updatedProducts.addAll(cart.getProducts().stream()
                        .map(product -> new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getUnitPrice(),
                                product.getQuantity(),
                                product.getTotalPrice(),
                                product.getChefId()
                        )).collect(Collectors.toList()));
                groupedOrder.setProducts(updatedProducts);
                groupedOrder.setTotalPrice(updatedProducts.stream().mapToDouble(ProductDTO::getTotalPrice).sum());

                HttpEntity<OrderDTO> requestUpdate = new HttpEntity<>(groupedOrder);
                restTemplate.exchange(orderServiceUrl + "/" + groupedOrder.getId(), HttpMethod.PUT, requestUpdate, OrderDTO.class);
            } else {
                // Créer une nouvelle commande groupée
                groupedOrder = new OrderDTO();
                groupedOrder.setUserId(userId);
                groupedOrder.setProducts(cart.getProducts().stream()
                        .map(product -> new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getUnitPrice(),
                                product.getQuantity(),
                                product.getTotalPrice(),
                                product.getChefId()
                        )).collect(Collectors.toList()));
                groupedOrder.setTotalPrice(cart.getProducts().stream().mapToDouble(Product::getTotalPrice).sum());
                groupedOrder.setGrouped(true); // Indique qu'il s'agit d'une commande groupée
                groupedOrder.setChefId("grouped"); // Utilisez "grouped" pour les commandes groupées
                restTemplate.postForObject(orderServiceUrl, groupedOrder, OrderDTO.class);
            }

            // Supprimer le panier après le paiement
            deleteCart(userId);
        }
    }
}
