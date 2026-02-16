package com.moon.shop.product.repository;

import com.moon.shop.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryIgnoreCase(String category, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    Page<Product> findByCategoryIgnoreCaseAndNameContainingIgnoreCaseOrCategoryIgnoreCaseAndDescriptionContainingIgnoreCase(
            String categoryName, String nameKeyword, String categoryDescription, String descriptionKeyword, Pageable pageable);
}
