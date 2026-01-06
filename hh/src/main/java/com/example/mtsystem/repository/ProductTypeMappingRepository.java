package com.example.mtsystem.repository;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.ProductType;
import com.example.mtsystem.entity.ProductTypeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductTypeMappingRepository extends JpaRepository<ProductTypeMapping, Long> {
    List<ProductTypeMapping> findByProduct(Product product);
    List<ProductTypeMapping> findByType(ProductType type);
    void deleteByProductAndType(Product product, ProductType type);
}
