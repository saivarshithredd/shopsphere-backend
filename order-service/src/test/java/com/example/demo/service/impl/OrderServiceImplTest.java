package com.example.demo.service.impl;

import com.example.demo.client.CatalogClient;
import com.example.demo.dto.CartRequest;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductPageResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Order;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.messaging.OrderProducer;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private CartRepository cartRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private CatalogClient catalogClient;
    @Mock private OrderProducer orderProducer;
    @InjectMocks private OrderServiceImpl orderService;

    private CartRequest cartRequest;
    private ProductDTO productDTO;
    private Cart cart;

    @BeforeEach
    void setUp() {
        cartRequest = new CartRequest();
        cartRequest.setProductName("iPhone 15");
        cartRequest.setQuantity(2);

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("iPhone 15");
        productDTO.setPrice(999.0);

        cart = new Cart();
        cart.setUserEmail("user@test.com");
        cart.setProductId(1L);
        cart.setProductName("iPhone 15");
        cart.setQuantity(2);
        cart.setPrice(999.0);
    }

    // ── addToCart ─────────────────────────────────────────────────

    @Test
    void addToCart_success() {
        ProductPageResponse response = new ProductPageResponse();
        response.setContent(List.of(productDTO));
        when(catalogClient.getProductByName("iPhone 15")).thenReturn(response);
        when(cartRepository.save(any())).thenReturn(cart);

        String result = orderService.addToCart("user@test.com", cartRequest);
        assertTrue(result.contains("iPhone 15"));
    }

    @Test
    void addToCart_productNotFound_throwsResourceNotFound() {
        ProductPageResponse response = new ProductPageResponse();
        response.setContent(Collections.emptyList());
        when(catalogClient.getProductByName("iPhone 15")).thenReturn(response);

        assertThrows(ResourceNotFoundException.class,
            () -> orderService.addToCart("user@test.com", cartRequest));
    }

    @Test
    void addToCart_nullResponse_throwsResourceNotFound() {
        when(catalogClient.getProductByName("iPhone 15")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,
            () -> orderService.addToCart("user@test.com", cartRequest));
    }

    // ── viewCart ──────────────────────────────────────────────────

    @Test
    void viewCart_success() {
        when(cartRepository.findByUserEmail("user@test.com")).thenReturn(List.of(cart));
        List<?> result = orderService.viewCart("user@test.com");
        assertEquals(1, result.size());
    }

    @Test
    void viewCart_empty() {
        when(cartRepository.findByUserEmail("user@test.com")).thenReturn(Collections.emptyList());
        List<?> result = orderService.viewCart("user@test.com");
        assertTrue(result.isEmpty());
    }

    // ── placeOrder ────────────────────────────────────────────────

    @Test
    void placeOrder_success() {
        when(cartRepository.findByUserEmail("user@test.com")).thenReturn(List.of(cart));
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setUserEmail("user@test.com");
        savedOrder.setStatus("PLACED");
        savedOrder.setTotalAmount(1998.0);
        when(orderRepository.save(any())).thenReturn(savedOrder);
        doNothing().when(orderProducer).sendOrderEvent(any());

        Order result = orderService.placeOrder("user@test.com");
        assertEquals("PLACED", result.getStatus());
        assertEquals(1998.0, result.getTotalAmount());
    }

    @Test
    void placeOrder_emptyCart_throwsBadRequest() {
        when(cartRepository.findByUserEmail("user@test.com")).thenReturn(Collections.emptyList());
        assertThrows(BadRequestException.class,
            () -> orderService.placeOrder("user@test.com"));
    }

    // ── getMyOrders ───────────────────────────────────────────────

    @Test
    void getMyOrders_success() {
        Order order = new Order();
        order.setUserEmail("user@test.com");
        when(orderRepository.findByUserEmail("user@test.com")).thenReturn(List.of(order));
        List<Order> result = orderService.getMyOrders("user@test.com");
        assertEquals(1, result.size());
    }

    // ── updateStatus ──────────────────────────────────────────────

    @Test
    void updateStatus_success() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PLACED");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.updateStatus(1L, "SHIPPED");
        assertEquals("SHIPPED", result.getStatus());
    }

    @Test
    void updateStatus_notFound_throwsResourceNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
            () -> orderService.updateStatus(99L, "SHIPPED"));
    }
}