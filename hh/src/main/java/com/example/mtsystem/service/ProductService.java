package com.example.mtsystem.service;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.ProductAttribute;
import com.example.mtsystem.entity.ProductType;
import com.example.mtsystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductAttributeService productAttributeService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByType(ProductType type) {
        return productTypeService.getProductsByType(type);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<ProductType> getProductTypes(Product product) {
        return productTypeService.getProductTypes(product);
    }

    public boolean addProductToType(Long productId, Long productTypeId) {
        Product product = getProductById(productId);
        ProductType type = productTypeService.findTypeById(productTypeId);
        if (product != null && type != null) {
            return productTypeService.addProductToType(product, type);
        }
        return false;
    }

    public boolean removeProductFromType(Long productId, Long productTypeId) {
        Product product = getProductById(productId);
        ProductType type = productTypeService.findTypeById(productTypeId);
        if (product != null && type != null) {
            return productTypeService.removeProductFromType(product, type);
        }
        return false;
    }

    // 添加商品属性
    public boolean addProductAttribute(Long productId, String name, String value, String description) {
        Product product = getProductById(productId);
        if (product != null) {
            ProductAttribute attribute = productAttributeService.addAttribute(name, value, description, product);
            return attribute != null;
        }
        return false;
    }

    // 获取商品的特定属性
    public List<ProductAttribute> getProductAttributes(Long productId, String attributeName) {
        Product product = getProductById(productId);
        if (product != null) {
            return productAttributeService.getAttributesByProductAndName(product, attributeName);
        }
        return null;
    }

    // 获取所有商品属性
    public List<ProductAttribute> getAllProductAttributes(Long productId) {
        Product product = getProductById(productId);
        if (product != null) {
            return productAttributeService.getAttributesByProduct(product);
        }
        return null;
    }

    // 删除商品的特定属性
    public boolean removeProductAttribute(Long productId, String attributeName) {
        Product product = getProductById(productId);
        if (product != null) {
            productAttributeService.removeAttributesByProductAndName(product, attributeName);
            return true;
        }
        return false;
    }
}
