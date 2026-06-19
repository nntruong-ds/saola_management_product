package com.example.demo36.Service.DTO;

import com.example.demo36.Entity.Product;
import vn.saolasoft.base.service.dto.DtoGet;

import java.io.Serial;


public class DtoProductGet extends DtoGet<Product, Long> {
    @Serial
    private static final long serialVersionUID = 1L;
    private String sku;
    private String categorycode;
    private String name;
    private Double price;
    
    public DtoProductGet() {}
    
    public DtoProductGet(Product product) {
        super(product);
    }
    
    @Override
    public void parse(Product product) {
        this.categorycode = product.getCategorycode();
        this.sku = product.getSku();
        this.price = product.getPrice();
        this.name = product.getName();
    }
    
    // Getter/Setter cho JSON mapping
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getCategorycode() { return categorycode; }
    public void setCategorycode(String categorycode) { this.categorycode = categorycode; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
