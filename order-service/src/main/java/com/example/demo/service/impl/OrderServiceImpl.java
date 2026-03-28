package com.example.demo.service.impl;

import com.example.demo.client.CatalogClient;
import com.example.demo.dto.CartRequest;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductPageResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.event.OrderEvent;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.messaging.OrderProducer;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private CartRepository cartRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private CatalogClient catalogClient;
    @Autowired private OrderProducer orderProducer;

    @Override
    public String addToCart(String email, CartRequest request) {
        ProductPageResponse response = catalogClient.getProductByName(request.getProductName());

        if (response == null || response.getContent() == null || response.getContent().isEmpty()) {
            throw new ResourceNotFoundException("Product not found: " + request.getProductName());
        }

        ProductDTO product = response.getContent().get(0);

        Cart cart = new Cart();
        cart.setUserEmail(email);
        cart.setProductId(product.getId());
        cart.setProductName(product.getName());
        cart.setQuantity(request.getQuantity());
        cart.setPrice(product.getPrice());

        cartRepository.save(cart);

        return "Added to cart: " + product.getName() + " (₹" + product.getPrice() + ")";
    }

    @Override
    public List<?> viewCart(String email) {
        return cartRepository.findByUserEmail(email);
    }

    @Transactional
    @Override
    public Order placeOrder(String email) {
        List<Cart> cartItems = cartRepository.findByUserEmail(email);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty for: " + email);
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (Cart cart : cartItems) {
            OrderItem item = new OrderItem();
            item.setProductId(cart.getProductId());
            item.setProductName(cart.getProductName());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getPrice());
            total += cart.getQuantity() * cart.getPrice();
            orderItems.add(item);
        }

        Order order = new Order();
        order.setUserEmail(email);
        order.setStatus("PLACED");
        order.setTotalAmount(total);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartRepository.deleteAll(cartItems);

        orderProducer.sendOrderEvent(new OrderEvent(
            savedOrder.getId(),
            savedOrder.getUserEmail(),
            savedOrder.getTotalAmount(),
            savedOrder.getStatus()
        ));

        return savedOrder;
    }

    @Override
    public List<Order> getMyOrders(String email) {
        return orderRepository.findByUserEmail(email);
    }

    @Override
    public Order updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        order.setStatus(status);
        return orderRepository.save(order);
    }
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}