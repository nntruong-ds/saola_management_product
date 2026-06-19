package com.example.demo36.Service.DTO;

import vn.saolasoft.base.service.dto.SerialIDDtoCreate;
import com.example.demo36.Entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serial;


public class DtoProductCreate extends SerialIDDtoCreate<Product> {
    @Serial
    public static final long serialVersionUID = 1L;
    
    @NotBlank(message = "SKU không được trống")
    private String sku;
    
    @NotBlank(message = "Mã danh mục không được trống")
    private String categorycode;
    
    @NotBlank(message = "Tên sản phẩm không được trống")
    private String name;
    
    @NotNull(message = "Giá không được null")
    @Positive(message = "Giá phải > 0")
    private Double price;
    
    public DtoProductCreate() {}
    
    public DtoProductCreate(String sku, String categorycode, String name, Double price) {
        this.price = price;
        this.categorycode = categorycode;
        this.name = name;
        this.sku = sku;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategorycode() { return categorycode; }
    public void setCategorycode(String categorycode) { this.categorycode = categorycode; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    @Override
    public Product toEntry() {
        Product p = new Product();
        p.setCategorycode(this.getCategorycode());
        p.setSku(this.getSku());
        p.setName(this.getName());;
        p.setPrice(this.getPrice());
        return p;
    }
}
