package com.shopsphere.catalog.service.impl;

import com.shopsphere.catalog.dto.ProductDTO;
import com.shopsphere.catalog.entity.*;
import com.shopsphere.catalog.exception.BadRequestException;
import com.shopsphere.catalog.exception.ResourceNotFoundException;
import com.shopsphere.catalog.repository.CategoryRepository;
import com.shopsphere.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private ProductServiceImpl productService;

    private PhoneProduct phoneProduct;
    private ProductDTO phoneDTO;

    @BeforeEach
    void setUp() {
        phoneProduct = new PhoneProduct();
        phoneProduct.setId(1L);
        phoneProduct.setName("iPhone 15");
        phoneProduct.setPrice(999.0);
        phoneProduct.setStock(10);
        phoneProduct.setBrand("Apple");
        phoneProduct.setStorageGB(128);
        phoneProduct.setRamGB(6);
        phoneProduct.setColor("Black");

        phoneDTO = new ProductDTO();
        phoneDTO.setProductType("PHONE");
        phoneDTO.setName("iPhone 15");
        phoneDTO.setPrice(999.0);
        phoneDTO.setStock(10);
        phoneDTO.setBrand("Apple");
        phoneDTO.setStorageGB(128);
        phoneDTO.setRamGB(6);
        phoneDTO.setColor("Black");
    }

    // ── createProduct ─────────────────────────────────────────────

    @Test
    void createProduct_success() {
        when(productRepository.save(any())).thenReturn(phoneProduct);
        ProductDTO result = productService.createProduct(phoneDTO);
        assertNotNull(result);
        assertEquals("iPhone 15", result.getName());
    }

    @Test
    void createProduct_missingName_throwsBadRequest() {
        phoneDTO.setName(null);
        assertThrows(BadRequestException.class, () -> productService.createProduct(phoneDTO));
    }

    @Test
    void createProduct_missingProductType_throwsBadRequest() {
        phoneDTO.setProductType(null);
        assertThrows(BadRequestException.class, () -> productService.createProduct(phoneDTO));
    }

    @Test
    void createProduct_unknownProductType_throwsBadRequest() {
        phoneDTO.setProductType("UNKNOWN");
        assertThrows(BadRequestException.class, () -> productService.createProduct(phoneDTO));
    }

    @Test
    void createProduct_categoryNotFound_throwsResourceNotFound() {
        phoneDTO.setCategoryId(99L);
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(phoneDTO));
    }

    // ── getProductById ────────────────────────────────────────────

    @Test
    void getProductById_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(phoneProduct));
        ProductDTO result = productService.getProductById(1L);
        assertEquals("iPhone 15", result.getName());
    }

    @Test
    void getProductById_notFound_throwsResourceNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99L));
    }

    // ── getAllProducts ────────────────────────────────────────────

    @Test
    void getAllProducts_success() {
        Page<Product> page = new PageImpl<>(List.of(phoneProduct));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<ProductDTO> result = productService.getAllProducts(0, 5, "id");
        assertEquals(1, result.getTotalElements());
    }

    // ── searchProducts ────────────────────────────────────────────

    @Test
    void searchProducts_success() {
        Page<Product> page = new PageImpl<>(List.of(phoneProduct));
        when(productRepository.findByNameContainingIgnoreCase(eq("iPhone"), any(Pageable.class)))
            .thenReturn(page);
        Page<ProductDTO> result = productService.searchProducts("iPhone", 0, 5);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchProducts_emptyKeyword_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> productService.searchProducts("", 0, 5));
    }

    // ── updateProduct ─────────────────────────────────────────────

    @Test
    void updateProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(phoneProduct));
        when(productRepository.save(any())).thenReturn(phoneProduct);
        ProductDTO result = productService.updateProduct(1L, phoneDTO);
        assertNotNull(result);
    }

    @Test
    void updateProduct_notFound_throwsResourceNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(99L, phoneDTO));
    }

    // ── deleteProduct ─────────────────────────────────────────────

    @Test
    void deleteProduct_success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_notFound_throwsResourceNotFound() {
        when(productRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(99L));
    }
}