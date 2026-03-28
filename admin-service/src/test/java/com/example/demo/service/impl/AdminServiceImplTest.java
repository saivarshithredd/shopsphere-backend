package com.example.demo.service.impl;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductPageResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.feign.CatalogClient;
import com.example.demo.feign.OrderClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock private CatalogClient catalogClient;
    @Mock private OrderClient orderClient;
    @InjectMocks private AdminServiceImpl adminService;

    private ProductDTO productDTO;
    private ProductPageResponse pageResponse;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("iPhone 15");
        productDTO.setPrice(999.0);
        productDTO.setStock(10);

        pageResponse = new ProductPageResponse();
    }

    // ── getAllProducts ────────────────────────────────────────────

    @Test
    void getAllProducts_success() {
        pageResponse.setContent(List.of(productDTO));
        when(catalogClient.getAllProducts()).thenReturn(pageResponse);
        List<ProductDTO> result = adminService.getAllProducts();
        assertEquals(1, result.size());
    }

    @Test
    void getAllProducts_empty_throwsResourceNotFound() {
        pageResponse.setContent(Collections.emptyList());
        when(catalogClient.getAllProducts()).thenReturn(pageResponse);
        assertThrows(ResourceNotFoundException.class, () -> adminService.getAllProducts());
    }

    // ── createProduct ─────────────────────────────────────────────

    @Test
    void createProduct_success() {
        when(catalogClient.createProduct(any())).thenReturn(productDTO);
        ProductDTO result = adminService.createProduct(productDTO);
        assertEquals("iPhone 15", result.getName());
    }

    @Test
    void createProduct_nullName_throwsBadRequest() {
        productDTO.setName(null);
        assertThrows(BadRequestException.class, () -> adminService.createProduct(productDTO));
    }

    @Test
    void createProduct_blankName_throwsBadRequest() {
        productDTO.setName("  ");
        assertThrows(BadRequestException.class, () -> adminService.createProduct(productDTO));
    }

    @Test
    void createProduct_nullProduct_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> adminService.createProduct(null));
    }

    // ── updateProduct ─────────────────────────────────────────────

    @Test
    void updateProduct_success() {
        when(catalogClient.updateProduct(eq(1L), any())).thenReturn(productDTO);
        ProductDTO result = adminService.updateProduct(1L, productDTO);
        assertNotNull(result);
    }

    @Test
    void updateProduct_invalidId_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> adminService.updateProduct(0L, productDTO));
    }

    @Test
    void updateProduct_nullProduct_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> adminService.updateProduct(1L, null));
    }

    // ── deleteProduct ─────────────────────────────────────────────

    @Test
    void deleteProduct_success() {
        doNothing().when(catalogClient).deleteProduct(1L);
        assertDoesNotThrow(() -> adminService.deleteProduct(1L));
        verify(catalogClient).deleteProduct(1L);
    }

    @Test
    void deleteProduct_invalidId_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> adminService.deleteProduct(0L));
    }

    // ── getAllOrders ──────────────────────────────────────────────

    @Test
    void getAllOrders_success() {
        when(orderClient.getAllOrders()).thenReturn(List.of(new Object()));
        List<Object> result = adminService.getAllOrders();
        assertEquals(1, result.size());
    }

    @Test
    void getAllOrders_empty_throwsResourceNotFound() {
        when(orderClient.getAllOrders()).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> adminService.getAllOrders());
    }

    // ── updateOrderStatus ─────────────────────────────────────────

    @Test
    void updateOrderStatus_success() {
        when(orderClient.updateStatus(1L, "SHIPPED")).thenReturn(new Object());
        Object result = adminService.updateOrderStatus(1L, "SHIPPED");
        assertNotNull(result);
    }

    @Test
    void updateOrderStatus_invalidId_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> adminService.updateOrderStatus(0L, "SHIPPED"));
    }

    @Test
    void updateOrderStatus_blankStatus_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> adminService.updateOrderStatus(1L, ""));
    }
}