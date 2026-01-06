package com.example.mtsystem.service;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.ProductType;
import com.example.mtsystem.entity.ProductTypeMapping;
import com.example.mtsystem.repository.ProductTypeMappingRepository;
import com.example.mtsystem.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductTypeMappingRepository productTypeMappingRepository;

    public ProductType createType(String name, String description) {
        if (productTypeRepository.findByName(name).isPresent()) {
            return null; // 类型已存在
        }
        ProductType type = new ProductType(name, description);
        return productTypeRepository.save(type);
    }

    public List<ProductType> getAllTypes() {
        return productTypeRepository.findAll();
    }

    public List<ProductType> getProductTypes(Product product) {
        return productTypeMappingRepository.findByProduct(product).stream()
                .map(ProductTypeMapping::getType)
                .toList();
    }

    public List<Product> getProductsByType(ProductType type) {
        return productTypeMappingRepository.findByType(type).stream()
                .map(ProductTypeMapping::getProduct)
                .toList();
    }

    public boolean addProductToType(Product product, ProductType type) {
        // 检查是否已存在关联
        List<ProductTypeMapping> existingMappings = productTypeMappingRepository.findByProduct(product);
        for (ProductTypeMapping mapping : existingMappings) {
            if (mapping.getType().getId().equals(type.getId())) {
                return false; // 已存在关联
            }
        }

        ProductTypeMapping mapping = new ProductTypeMapping(product, type);
        productTypeMappingRepository.save(mapping);
        return true;
    }

    public boolean removeProductFromType(Product product, ProductType type) {
        productTypeMappingRepository.deleteByProductAndType(product, type);
        return true;
    }

    public ProductType findTypeById(Long id) {
        return productTypeRepository.findById(id).orElse(null);
    }

    public ProductType findTypeByName(String name) {
        return productTypeRepository.findByName(name).orElse(null);
    }
}
