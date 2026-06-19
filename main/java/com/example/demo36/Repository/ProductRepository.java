package com.example.demo36.Repository;
import com.example.demo36.Entity.Product;
import org.springframework.stereotype.Repository;
import vn.saolasoft.base.persistence.repository.VoidableRepository;
@Repository
public interface ProductRepository extends VoidableRepository<Product, Long>{
    @Override
    boolean existsByIdAndVoidedFalse(Long id);


    @Override
    Product findByIdAndVoidedFalse(Long id);
}
