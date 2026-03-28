package com.shopsphere.catalog.service;

import com.shopsphere.catalog.dto.ProductDTO;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    Page<ProductDTO> getAllProducts(int page, int size, String sortBy);

    Page<ProductDTO> searchProducts(String keyword, int page, int size);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);
}