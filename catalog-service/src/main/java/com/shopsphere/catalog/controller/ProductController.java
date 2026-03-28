package com.shopsphere.catalog.controller;

import com.shopsphere.catalog.dto.ProductDTO;
import com.shopsphere.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ── Public endpoints ──────────────────────────────────────────

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public Page<ProductDTO> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return productService.getAllProducts(page, size, sortBy);
    }

    @GetMapping("/search")
    public Page<ProductDTO> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return productService.searchProducts(keyword, page, size);
    }

    // ── Internal endpoints (called only by admin-service via Feign) ──
    // Not annotated with @PreAuthorize here — security is enforced
    // at the SecurityConfig level (hasRole ADMIN).
    // These won't appear in admin-service Swagger since they live
    // in catalog-service only.

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}

