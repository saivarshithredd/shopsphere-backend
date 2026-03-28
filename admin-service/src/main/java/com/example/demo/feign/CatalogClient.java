package com.example.demo.feign;

import com.example.demo.config.FeignConfig;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "CATALOG-SERVICE", configuration = FeignConfig.class)
public interface CatalogClient {

    @GetMapping("/products")
    ProductPageResponse getAllProducts();

    @PostMapping("/products")
    ProductDTO createProduct(@RequestBody ProductDTO product);

    @PutMapping("/products/{id}")
    ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO product);

    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable Long id);
}