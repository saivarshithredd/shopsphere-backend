package com.example.demo.service.impl;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductPageResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.feign.CatalogClient;
import com.example.demo.feign.OrderClient;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private CatalogClient catalogClient;

    @Autowired
    private OrderClient orderClient;

    @Override
    public List<ProductDTO> getAllProducts() {
        ProductPageResponse response = catalogClient.getAllProducts();
        List<ProductDTO> products = response.getContent();
        if (products == null || products.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
        return products;
    }

    @Override
    public ProductDTO createProduct(ProductDTO product) {
        if (product == null || product.getName() == null || product.getName().isBlank()) {
            throw new BadRequestException("Product name is required");
        }
        return catalogClient.createProduct(product);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid product id: " + id);
        }
        if (product == null) {
            throw new BadRequestException("Product data is required");
        }
        return catalogClient.updateProduct(id, product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid product id: " + id);
        }
        catalogClient.deleteProduct(id);
    }

    @Override
    public List<Object> getAllOrders() {
        List<Object> orders = orderClient.getAllOrders();
        if (orders == null || orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found");
        }
        return orders;
    }

    @Override
    public Object updateOrderStatus(Long id, String status) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid order id: " + id);
        }
        if (status == null || status.isBlank()) {
            throw new BadRequestException("Order status is required");
        }
        return orderClient.updateStatus(id, status);
    }
}