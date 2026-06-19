package com.example.demo36.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;

import vn.saolasoft.base.persistence.model.VoidableSerialIDEntry;
@Entity
@Table(name="MyProduct")


public class Product extends VoidableSerialIDEntry{
    @Column(name="sku", nullable = false, unique = true)
    private String sku;
    @Column(name = "categorycode", nullable = false)
    private String categorycode;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private Double price;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategorycode() { return categorycode; }
    public void setCategorycode(String categorycode) { this.categorycode = categorycode; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
