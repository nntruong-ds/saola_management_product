package com.example.demo36.Service.DTO;

import vn.saolasoft.base.service.dto.DtoUpdate;
import com.example.demo36.Entity.Product;

import java.io.Serial;

public class DtoProductUpdate extends DtoUpdate<Product, Long> {
    @Serial
    private static final long serialVersionUID = 1L;
    private String sku;
    private String categorycode;
    private String name;
    private Double price;
    
    public DtoProductUpdate() {}
    
    public DtoProductUpdate(String sku, String categorycode, String name, Double price) {
        this.sku = sku;
        this.categorycode = categorycode;
        this.name = name;
        this.price = price;
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
    @Override
    public boolean apply(Product p) {
        boolean check = false;
        if(!this.name.equals(p.getName()) && this.name != null) {
            p.setName(this.name);
            check = true;
        }
        if(!this.categorycode.equals(p.getCategorycode()) && this.categorycode != null) {
            p.setCategorycode(this.categorycode);
            check = true;
        }
        if(this.price != p.getPrice() && this.price != null) {
            p.setPrice(this.price);
            check = true;
        }
        if(!this.sku.equals(p.getSku()) && this.sku != null) {
            p.setSku(this.sku);
            check = true;
        }
        return check;
    }
}
