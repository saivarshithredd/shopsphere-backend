package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import java.util.List;

public interface AdminService {
    List<ProductDTO> getAllProducts();
    ProductDTO createProduct(ProductDTO product);
    ProductDTO updateProduct(Long id, ProductDTO product);
    void deleteProduct(Long id);
    List<Object> getAllOrders();
    Object updateOrderStatus(Long id, String status);
}