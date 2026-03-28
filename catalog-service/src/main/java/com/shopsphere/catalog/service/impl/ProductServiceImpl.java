package com.shopsphere.catalog.service.impl;

import com.shopsphere.catalog.dto.ProductDTO;
import com.shopsphere.catalog.entity.*;
import com.shopsphere.catalog.exception.BadRequestException;
import com.shopsphere.catalog.exception.ResourceNotFoundException;
import com.shopsphere.catalog.repository.CategoryRepository;
import com.shopsphere.catalog.repository.ProductRepository;
import com.shopsphere.catalog.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ── ENTITY → DTO ──────────────────────────────────────────────────────────
    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setBrand(product.getBrand());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product instanceof PhoneProduct phone) {
            dto.setProductType("PHONE");
            dto.setStorageGB(phone.getStorageGB());
            dto.setRamGB(phone.getRamGB());
            dto.setColor(phone.getColor());

        } else if (product instanceof ClothingProduct clothing) {
            dto.setProductType("CLOTHING");
            dto.setSize(clothing.getSize());
            dto.setColor(clothing.getColor());
            dto.setMaterial(clothing.getMaterial());
        }

        return dto;
    }

    // ── DTO → ENTITY ──────────────────────────────────────────────────────────
    private Product mapToEntity(ProductDTO dto) {
        Product product;

        if ("PHONE".equalsIgnoreCase(dto.getProductType())) {
            PhoneProduct phone = new PhoneProduct();
            phone.setStorageGB(dto.getStorageGB() != null ? dto.getStorageGB() : 0);
            phone.setRamGB(dto.getRamGB() != null ? dto.getRamGB() : 0);
            phone.setColor(dto.getColor());
            product = phone;

        } else if ("CLOTHING".equalsIgnoreCase(dto.getProductType())) {
            ClothingProduct clothing = new ClothingProduct();
            clothing.setSize(dto.getSize());
            clothing.setColor(dto.getColor());
            clothing.setMaterial(dto.getMaterial());
            product = clothing;

        } else {
            throw new BadRequestException("Unknown productType: " + dto.getProductType()
                    + ". Valid values: PHONE, CLOTHING");
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setBrand(dto.getBrand());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id: " + dto.getCategoryId()));
            product.setCategory(category);
        }

        return product;
    }

    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        if (dto.getProductType() == null || dto.getProductType().isBlank()) {
            throw new BadRequestException("productType is required. Valid values: PHONE, CLOTHING");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BadRequestException("Product name is required");
        }
        Product product = mapToEntity(dto);
        return mapToDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return mapToDTO(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id)));
    }

    @Override
    public Page<ProductDTO> getAllProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    public Page<ProductDTO> searchProducts(String keyword, int page, int size) {
        if (keyword == null || keyword.isBlank()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        Pageable pageable = PageRequest.of(page, size);
        return productRepository
                .findByNameContainingIgnoreCase(keyword, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());
        existing.setBrand(dto.getBrand());

        if (existing instanceof PhoneProduct phone) {
            if (dto.getStorageGB() != null) phone.setStorageGB(dto.getStorageGB());
            if (dto.getRamGB() != null)     phone.setRamGB(dto.getRamGB());
            if (dto.getColor() != null)     phone.setColor(dto.getColor());

        } else if (existing instanceof ClothingProduct clothing) {
            if (dto.getSize() != null)     clothing.setSize(dto.getSize());
            if (dto.getColor() != null)    clothing.setColor(dto.getColor());
            if (dto.getMaterial() != null) clothing.setMaterial(dto.getMaterial());
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id: " + dto.getCategoryId()));
            existing.setCategory(category);
        }
        
        return mapToDTO(productRepository.save(existing));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}