package com.example.mtsystem.repository;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    List<ProductAttribute> findByProduct(Product product);
    List<ProductAttribute> findByProductAndName(Product product, String name);
    void deleteByProductAndName(Product product, String name);
}
