package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.client.CartClient;
import com.programmingtechie.productservice.client.WishlistClient;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    private GoogleDriveService googleDriveService;

    private final WishlistClient wishlistClient;

    @Autowired
    private CartClient cartClient;


    public void createProduct(ProductRequest productRequest, MultipartFile imageFile, String idChef)  throws IOException  {
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            // Convertir MultipartFile en File
            File file = convertMultipartFileToFile(imageFile);
            // Télécharger le fichier vers Google Drive et obtenir l'URL
            imageUrl = googleDriveService.uploadFile(file, imageFile.getContentType());
            // Supprimer le fichier temporaire
            file.delete();
        }
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .promoPrice(productRequest.getPromoPrice())
                .minTime(productRequest.getMinTime())
                .maxTime(productRequest.getMaxTime())
                .minQuantity(productRequest.getMinQuantity())
                .maxQuantity(productRequest.getMaxQuantity())
                .calories(productRequest.getCalories())
                .availability(productRequest.isAvailability())
                .idCategory(productRequest.getIdCategory())
                .validated(productRequest.isValidated())
                .wilaya(productRequest.getWilaya())
                .imageUrl(imageUrl)
                .idChef(idChef)
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }
    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        return mapToProductResponse(product);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .promoPrice(product.getPromoPrice())
                .minTime(product.getMinTime())
                .maxTime(product.getMaxTime())
                .minQuantity(product.getMinQuantity())
                .maxQuantity(product.getMaxQuantity())
                .calories(product.getCalories())
                .availability(product.isAvailability())
                .idCategory(product.getIdCategory())
                .validated(product.isValidated())
                .idChef(product.getIdChef())
                .wilaya(product.getWilaya())
                .imageUrl(product.getImageUrl())
                .build();
    }
    public void deleteProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        // Extract the file ID from the image URL
        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String fileId = imageUrl.substring(imageUrl.indexOf("id=") + 3);
            try {
                googleDriveService.deleteFile(fileId);
                log.info("Image with ID {} deleted from Google Drive", fileId);
            } catch (IOException e) {
                log.error("Failed to delete image from Google Drive", e);
            }
        }

        productRepository.delete(product);
        log.info("Product {} is deleted", productId);
        wishlistClient.removeProductFromAllWishlists(productId);
        cartClient.removeProductFromAllCarts(productId);
    }

    public void updateProduct(String productId, ProductRequest productRequest, MultipartFile imageFile) throws IOException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        String oldImageUrl = existingProduct.getImageUrl();
        String newImageUrl = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            // Convert MultipartFile to File
            java.io.File file = convertMultipartFileToFile(imageFile);
            // Upload the new file to Google Drive and get the new URL
            newImageUrl = googleDriveService.uploadFile(file, imageFile.getContentType());
            // Delete the temporary file
            file.delete();

            // Extract the file ID from the old image URL and delete the old image from Google Drive
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                String oldFileId = oldImageUrl.substring(oldImageUrl.indexOf("id=") + 3);
                googleDriveService.deleteFile(oldFileId);
            }

            // Update the product's image URL
            existingProduct.setImageUrl(newImageUrl);
        }

        // Update other product details
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setPromoPrice(productRequest.getPromoPrice());
        existingProduct.setMinTime(productRequest.getMinTime());
        existingProduct.setMaxTime(productRequest.getMaxTime());
        existingProduct.setMinQuantity(productRequest.getMinQuantity());
        existingProduct.setMaxQuantity(productRequest.getMaxQuantity());
        existingProduct.setCalories(productRequest.getCalories());
        existingProduct.setAvailability(productRequest.isAvailability());
        existingProduct.setIdCategory(productRequest.getIdCategory());
        existingProduct.setWilaya(productRequest.getWilaya());
        existingProduct.setValidated(false);

        productRepository.save(existingProduct);
        log.info("Product {} is updated", productId);
    }


    public List<ProductResponse> getValidatedProducts() {
        List<Product> products = productRepository.findByValidated(true); // Query for validated products
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductResponse> getUnvalidatedProducts() {
        List<Product> unvalidatedProducts = productRepository.findByValidated(false);
        return unvalidatedProducts.stream().map(this::mapToProductResponse).toList();
    }

    public void validateProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        product.setValidated(true);
        productRepository.save(product);
        log.info("Product {} is validated", productId);
    }
    public List<ProductResponse> getValidatedProductsByIdCategory(String idCategory,String wilaya) {
        List<Product> products = productRepository.findByValidatedAndIdCategoryAndWilaya(true, idCategory,wilaya);
        return products.stream().map(this::mapToProductResponse).toList();
    }
    public List<ProductResponse> getProductsByIdChef(String idChef) {
        List<Product> products = productRepository.findByIdChef(idChef);
        return products.stream().map(this::mapToProductResponse).toList();
    }
    public List<ProductResponse> searchProductsByNameIgnoreCaseContaining(String name, String wilaya) {
        List<Product> products = productRepository.findByNameIgnoreCaseContainingAndValidatedAndWilaya(name, true,wilaya); // Query for validated products by name ignoring case
        return products.stream().map(this::mapToProductResponse).toList();
    }
    public List<ProductResponse> searchProductsByWilaya(String wilaya) {
        List<Product> products = productRepository.findByWilayaAndValidated(wilaya, true);
        return products.stream().map(this::mapToProductResponse).toList();
    }
    public void updateProductAvailability(String productId, boolean availability) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setAvailability(availability);
        productRepository.save(product);
    }

    public List<ProductResponse> getProductsByIdChefAndNameIgnoreCaseContaining(String idChef,String name) {
        List<Product> products = productRepository.findByIdChefAndNameIgnoreCaseContaining(idChef,name);
        return products.stream().map(this::mapToProductResponse).toList();
    }


}