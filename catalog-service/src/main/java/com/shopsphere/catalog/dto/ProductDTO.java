package com.shopsphere.catalog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)  // hides null fields in response
public class ProductDTO {

    private Long id;
    private String productType;   // "PHONE" or "CLOTHING"
    private String name;
    private String description;
    private double price;
    private int stock;
    private String brand;

    private Long categoryId;
    private String categoryName;

    // --- Phone-specific (null if not a phone) ---
    private Integer storageGB;
    private Integer ramGB;
    private String color;

    // --- Clothing-specific (null if not clothing) ---
    private String size;
    private String material;

    public ProductDTO() {}

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Integer getStorageGB() { return storageGB; }
    public void setStorageGB(Integer storageGB) { this.storageGB = storageGB; }

    public Integer getRamGB() { return ramGB; }
    public void setRamGB(Integer ramGB) { this.ramGB = ramGB; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
}