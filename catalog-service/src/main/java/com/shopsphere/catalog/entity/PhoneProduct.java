package com.shopsphere.catalog.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phone_product")
@DiscriminatorValue("PHONE")
public class PhoneProduct extends Product {

    private int storageGB;   // e.g. 128, 256, 512
    private int ramGB;       // e.g. 8, 12, 16
    private String color;    // e.g. "Midnight Black"

    public PhoneProduct() {}

    public int getStorageGB() { return storageGB; }
    public void setStorageGB(int storageGB) { this.storageGB = storageGB; }

    public int getRamGB() { return ramGB; }
    public void setRamGB(int ramGB) { this.ramGB = ramGB; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}