package com.programmingtechie.wishlistservice.service;

import com.programmingtechie.wishlistservice.client.ProductClient;
import com.programmingtechie.wishlistservice.model.Wishlist;
import com.programmingtechie.wishlistservice.model.WishlistItem;
import com.programmingtechie.wishlistservice.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductClient productClient;

    private Wishlist getOrCreateWishlist(String userId) {
        return wishlistRepository.findByUserId(userId).orElseGet(() -> {
            Wishlist wishlist = Wishlist.builder().userId(userId).build();
            return wishlistRepository.save(wishlist);
        });
    }

    public Optional<Wishlist> getWishlist(String userId) {
        Wishlist wishlist = getOrCreateWishlist(userId);

        // Filter out unvalidated products
        List<WishlistItem> validatedItems = wishlist.getItems().stream()
                .filter(item -> isProductValidated(item.getProductId()))
                .collect(Collectors.toList());

        // Set the filtered list back to the wishlist
        wishlist.setItems(validatedItems);

        return Optional.of(wishlist);
    }

    private boolean isProductValidated(String productId) {
        ProductClient.ProductResponse productResponse = productClient.getProductById(productId);
        return productResponse != null && productResponse.isValidated();
    }

    public Wishlist addItemToWishlist(String userId, String productId) {
        ProductClient.ProductResponse product = productClient.getProductById(productId);
        Wishlist wishlist = getOrCreateWishlist(userId);

        // Check if the product is already in the wishlist
        boolean productExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(productId));

        if (productExists) {
            return wishlist;
        }


        WishlistItem item = WishlistItem.builder()
                .productId(product.getId())
                .name(product.getName())
                .imageUrl(product.getImageUrl())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();


        wishlist.getItems().add(item);
        wishlistRepository.save(wishlist);
        return wishlist;
    }

    public Wishlist removeItemFromWishlist(String userId, String productId) {
        Wishlist wishlist = getOrCreateWishlist(userId);
        wishlist.getItems().removeIf(item -> item.getProductId().equals(productId));

        if (wishlist.getItems().isEmpty()) {
            wishlistRepository.delete(wishlist);
        } else {
            wishlistRepository.save(wishlist);
        }
        return wishlist;
    }

    public void removeProductFromAllWishlists(String productId) {
        List<Wishlist> wishlists = wishlistRepository.findAll();
        for (Wishlist wishlist : wishlists) {
            wishlist.getItems().removeIf(item -> item.getProductId().equals(productId));
            if (wishlist.getItems().isEmpty()) {
                wishlistRepository.delete(wishlist);
            } else {
                wishlistRepository.save(wishlist);
            }
        }
    }


}
