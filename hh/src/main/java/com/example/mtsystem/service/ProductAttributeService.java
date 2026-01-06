package com.example.mtsystem.service;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.ProductAttribute;
import com.example.mtsystem.repository.ProductAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAttributeService {

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    public ProductAttribute addAttribute(String name, String value, String description, Product product) {
        ProductAttribute attribute = new ProductAttribute(name, value, description, product);
        return productAttributeRepository.save(attribute);
    }

    public List<ProductAttribute> getAttributesByProduct(Product product) {
        return productAttributeRepository.findByProduct(product);
    }

    public List<ProductAttribute> getAttributesByProductAndName(Product product, String name) {
        return productAttributeRepository.findByProductAndName(product, name);
    }

    public void removeAttributesByProductAndName(Product product, String name) {
        productAttributeRepository.deleteByProductAndName(product, name);
    }
}
