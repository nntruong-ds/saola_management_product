package com.example.demo36.Service.Impl;

import com.example.demo36.Service.DTO.DtoProductGet;
import com.example.demo36.Entity.Product;
import com.example.demo36.Repository.ProductRepository;
import com.example.demo36.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.saolasoft.base.service.impl.VoidableDtoJpaServiceImpl;

import java.util.Set;

@Service
public class ProductServiceImpl
        extends VoidableDtoJpaServiceImpl<DtoProductGet, Product, Long>
        implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Framework yêu cầu biết Repository cụ thể để thực hiện các thao tác DB.
    @Override
    public ProductRepository getRepository() {
        return productRepository;
    }

    // Framework gọi method này sau mỗi lần lấy Entity từ DB
    // để chuyển Entity → DTO trước khi trả về controller.
    @Override
    public DtoProductGet convert(Product product) {
        return new DtoProductGet(product);
    }

    // Khai báo các cột cho phép client sắp xếp (ORDER BY).
    // Chỉ những cột trong Set này mới được chấp nhận, tránh lộ cột nội bộ. (Hard Separate Entity & API)
    // Nếu không hợp lệ / rỗng → fallback về không sort.
    @Override
    public Set<String> getSortableColumns() {
        return Set.of("id", "name", "price", "quantity", "dateCreated");
    }
}
