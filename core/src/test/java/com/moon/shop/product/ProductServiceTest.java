package com.moon.shop.product; // Changed package to com.moon.shop.product

import com.moon.shop.product.domain.Product;
import com.moon.shop.product.dto.CreateProductRequest;
import com.moon.shop.product.dto.UpdateProductRequest;
import com.moon.shop.product.repository.ProductRepository;
import com.moon.shop.product.service.ProductService; // Keep this import for the service
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private CreateProductRequest createRequest;
    private UpdateProductRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Sample Product entity
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .originalPrice(10000)
                .discountPrice(8000)
                .discountRate(20)
                .thumbnailImage("thumb.jpg")
                .category("Electronics")
                .stock(100)
                .images(Arrays.asList("img1.jpg", "img2.jpg"))
                .description("Test description")
                .brand("Test Brand")
                .specs("{\"screen\": \"6.1\", \"color\": \"black\"}") // Stored as JSON string - FIXED SYNTAX ERROR
                .createdAt(LocalDateTime.now())
                .build();

        createRequest = CreateProductRequest.builder()
                .name("New Product")
                .originalPrice(15000)
                .discountPrice(12000)
                .discountRate(20)
                .thumbnailImage("new_thumb.jpg")
                .category("Books")
                .stock(50)
                .images(Arrays.asList("new_img1.jpg"))
                .description("New product description")
                .brand("New Brand")
                .specs(Map.of("pages", 300, "author", "Test Author"))
                .build();

        // Sample UpdateProductRequest DTO
        updateRequest = UpdateProductRequest.builder()
                .name("Updated Product")
                .originalPrice(12000)
                .discountPrice(10000)
                .discountRate(17)
                .thumbnailImage("updated_thumb.jpg")
                .category("Updated Category")
                .stock(120)
                .images(Arrays.asList("updated_img1.jpg", "updated_img2.jpg"))
                .description("Updated description")
                .brand("Updated Brand")
                .specs(Map.of("weight", "1kg", "color", "blue"))
                .build();
    }

    @Test
    @DisplayName("제품 생성 - 성공")
    void createProduct_success() {
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        when(productRepository.save(productCaptor.capture())).thenReturn(product); // Capture the argument

        Product createdProduct = productService.createProduct(createRequest);

        assertThat(createdProduct).isNotNull();
        // Assert against the captured product's properties, which should match createRequest
        Product capturedProduct = productCaptor.getValue();
        assertThat(capturedProduct.getName()).isEqualTo(createRequest.getName());
        assertThat(capturedProduct.getOriginalPrice()).isEqualTo(createRequest.getOriginalPrice());
        assertThat(capturedProduct.getDiscountPrice()).isEqualTo(createRequest.getDiscountPrice());
        assertThat(capturedProduct.getDiscountRate()).isEqualTo(createRequest.getDiscountRate());
        assertThat(capturedProduct.getThumbnailImage()).isEqualTo(createRequest.getThumbnailImage());
        assertThat(capturedProduct.getCategory()).isEqualTo(createRequest.getCategory());
        assertThat(capturedProduct.getStock()).isEqualTo(createRequest.getStock());
        assertThat(capturedProduct.getImages()).isEqualTo(createRequest.getImages());
        assertThat(capturedProduct.getDescription()).isEqualTo(createRequest.getDescription());
        assertThat(capturedProduct.getBrand()).isEqualTo(createRequest.getBrand());
        assertThat(capturedProduct.getSpecs()).isNotNull(); // Basic check for specs

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("ID로 제품 조회 - 성공")
    void getProductById_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("ID로 제품 조회 - 실패 (제품 없음)")
    void getProductById_notFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.getProductById(99L));
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("모든 제품 조회 - 성공 (페이지네이션 및 필터링 없음)")
    void getAllProducts_success_noFiltering() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(product,
                Product.builder().id(2L).name("Another Product").build());
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        Page<Product> foundProducts = productService.getAllProducts(pageable, Optional.empty(), Optional.empty());

        assertThat(foundProducts).hasSize(2);
        assertThat(foundProducts.getContent().get(0).getName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }


    @Test
    @DisplayName("모든 제품 조회 - 성공 (카테고리 필터링)")
    void getAllProducts_success_withCategoryFiltering() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> electronicsProducts = Arrays.asList(product);
        Page<Product> productPage = new PageImpl<>(electronicsProducts, pageable, electronicsProducts.size());

        when(productRepository.findByCategoryIgnoreCase(eq("Electronics"), any(Pageable.class))).thenReturn(productPage);

        Page<Product> foundProducts = productService.getAllProducts(pageable, Optional.of("Electronics"), Optional.empty());

        assertThat(foundProducts).hasSize(1);
        assertThat(foundProducts.getContent().get(0).getCategory()).isEqualTo("Electronics");
        verify(productRepository, times(1)).findByCategoryIgnoreCase(eq("Electronics"), any(Pageable.class));
    }

    @Test
    @DisplayName("모든 제품 조회 - 성공 (검색어 필터링)")
    void getAllProducts_success_withSearchKeywordFiltering() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> matchingProducts = Arrays.asList(product);
        Page<Product> productPage = new PageImpl<>(matchingProducts, pageable, matchingProducts.size());

        when(productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(eq("Test"), eq("Test"), any(Pageable.class)))
                .thenReturn(productPage);

        Page<Product> foundProducts = productService.getAllProducts(pageable, Optional.empty(), Optional.of("Test"));

        assertThat(foundProducts).hasSize(1);
        assertThat(foundProducts.getContent().get(0).getName()).contains("Test");
        verify(productRepository, times(1)).findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(eq("Test"), eq("Test"), any(Pageable.class));
    }

    @Test
    @DisplayName("모든 제품 조회 - 성공 (카테고리 및 검색어 필터링)")
    void getAllProducts_success_withCategoryAndSearchKeywordFiltering() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> matchingProducts = Arrays.asList(product);
        Page<Product> productPage = new PageImpl<>(matchingProducts, pageable, matchingProducts.size());

        when(productRepository.findByCategoryIgnoreCaseAndNameContainingIgnoreCaseOrCategoryIgnoreCaseAndDescriptionContainingIgnoreCase(
                eq("Electronics"), eq("Test"), eq("Electronics"), eq("Test"), any(Pageable.class)))
                .thenReturn(productPage);

        Page<Product> foundProducts = productService.getAllProducts(pageable, Optional.of("Electronics"), Optional.of("Test"));

        assertThat(foundProducts).hasSize(1);
        assertThat(foundProducts.getContent().get(0).getCategory()).isEqualTo("Electronics");
        assertThat(foundProducts.getContent().get(0).getName()).contains("Test");
        verify(productRepository, times(1)).findByCategoryIgnoreCaseAndNameContainingIgnoreCaseOrCategoryIgnoreCaseAndDescriptionContainingIgnoreCase(
                eq("Electronics"), eq("Test"), eq("Electronics"), eq("Test"), any(Pageable.class));
    }

    @Test
    @DisplayName("제품 업데이트 - 성공")
    void updateProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, updateRequest);

        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getName()).isEqualTo(updateRequest.getName());
        assertThat(updatedProduct.getOriginalPrice()).isEqualTo(updateRequest.getOriginalPrice());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("제품 업데이트 - 실패 (제품 없음)")
    void updateProduct_notFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.updateProduct(99L, updateRequest));
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("제품 삭제 - 성공")
    void deleteProduct_success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("제품 삭제 - 실패 (제품 없음)")
    void deleteProduct_notFound() {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> productService.deleteProduct(99L));
        verify(productRepository, times(1)).existsById(anyLong());
        verify(productRepository, never()).deleteById(anyLong());
    }
}