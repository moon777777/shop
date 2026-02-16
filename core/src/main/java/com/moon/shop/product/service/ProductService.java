package com.moon.shop.product.service;

import com.moon.shop.product.domain.Product;
import com.moon.shop.product.dto.CreateProductRequest;
import com.moon.shop.product.dto.UpdateProductRequest;
import com.moon.shop.product.domain.HashMapConverter;
import com.moon.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // Added for createdAt
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional; // Added

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .originalPrice(request.getOriginalPrice())
                .discountPrice(request.getDiscountPrice())
                .discountRate(request.getDiscountRate())
                .thumbnailImage(request.getThumbnailImage())
                .category(request.getCategory())
                .stock(request.getStock())
                .images(request.getImages())
                .description(request.getDescription())
                .brand(request.getBrand())
                .specs(new HashMapConverter().convertToDatabaseColumn(request.getSpecs())) // Convert Map to String
                .createdAt(LocalDateTime.now()) // Set creation time
                .build();
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    public Page<Product> getAllProducts(Pageable pageable, Optional<String> category, Optional<String> searchKeyword) {
        if (category.isPresent() && searchKeyword.isPresent()) {
            return productRepository.findByCategoryIgnoreCaseAndNameContainingIgnoreCaseOrCategoryIgnoreCaseAndDescriptionContainingIgnoreCase(
                    category.get(), searchKeyword.get(), category.get(), searchKeyword.get(), pageable);
        } else if (category.isPresent()) {
            return productRepository.findByCategoryIgnoreCase(category.get(), pageable);
        } else if (searchKeyword.isPresent()) {
            return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    searchKeyword.get(), searchKeyword.get(), pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    @Transactional
    public Product updateProduct(Long id, UpdateProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));

        existingProduct.update(request);

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
