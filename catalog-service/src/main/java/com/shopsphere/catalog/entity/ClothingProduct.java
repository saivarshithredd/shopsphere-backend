package com.shopsphere.catalog.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clothing_product")
@DiscriminatorValue("CLOTHING")
public class ClothingProduct extends Product {

    private String size;      // e.g. "S", "M", "L", "XL", "XXL"
    private String color;     // e.g. "Red", "Blue"
    private String material;  // e.g. "Cotton", "Polyester"

    public ClothingProduct() {}

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
}