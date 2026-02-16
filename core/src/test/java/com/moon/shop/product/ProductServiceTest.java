package com.moon.shop.product;

import com.moon.shop.product.domain.Product;
import com.moon.shop.product.dto.UpdateProductRequest;
import com.moon.shop.product.repository.ProductRepository;
import com.moon.shop.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private UpdateProductRequest updateRequest;

    @BeforeEach
    void setUp() {
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
                .specs("{}") // Assuming specs are stored as JSON string
                .createdAt(LocalDateTime.now())
                .build();

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
                .specs(Map.of("weight", "1kg", "color", "blue")) // Assuming map input for specs
                .build();
    }

    @Test
    @DisplayName("제품 생성 - 성공")
    void createProduct_success() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo("Test Product");
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
    @DisplayName("모든 제품 조회 - 성공")
    void getAllProducts_success() {
        List<Product> products = Arrays.asList(product,
                Product.builder().id(2L).name("Another Product").build());
        when(productRepository.findAll()).thenReturn(products);

        List<Product> foundProducts = productService.getAllProducts();

        assertThat(foundProducts).hasSize(2);
        assertThat(foundProducts.get(0).getName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).findAll();
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
        // Verify that the product's update method was called (implicitly by checking state)
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
