package com.moon.shop.product.domain;

import com.moon.shop.product.dto.UpdateProductRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int originalPrice;
    private int discountPrice;
    private int discountRate;
    private String thumbnailImage;
    private String category;
    private int stock;

    @ElementCollection
    private List<String> images;

    @Lob
    private String description;

    private String brand;

    @Convert(converter = HashMapConverter.class)
    private String specs;

    private LocalDateTime createdAt;

    public void update(UpdateProductRequest request) {
        this.name = request.getName();
        this.originalPrice = request.getOriginalPrice();
        this.discountPrice = request.getDiscountPrice();
        this.discountRate = request.getDiscountRate();
        this.thumbnailImage = request.getThumbnailImage();
        this.category = request.getCategory();
        this.stock = request.getStock();
        this.images = request.getImages();
        this.description = request.getDescription();
        this.brand = request.getBrand();
        this.specs = new HashMapConverter().convertToDatabaseColumn(request.getSpecs());
    }
}
