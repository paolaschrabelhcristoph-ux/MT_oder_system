package com.example.mtsystem.controller;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.ProductType;
import com.example.mtsystem.service.ProductAttributeService;
import com.example.mtsystem.service.ProductService;
import com.example.mtsystem.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductAttributeService productAttributeService;

    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        List<ProductType> types = productTypeService.getAllTypes();
        model.addAttribute("products", products);
        model.addAttribute("types", types);
        model.addAttribute("productService", productService); // 添加 productService 到 model
        return "products";
    }

    @GetMapping("/products/type/{typeId}")
    public String showProductsByType(@PathVariable Long typeId, Model model) {
        ProductType type = productTypeService.findTypeById(typeId);
        if (type != null) {
            List<Product> products = productService.getProductsByType(type);
            model.addAttribute("products", products);
            model.addAttribute("typeName", type.getName());
            model.addAttribute("productService", productService); // 添加 productService 到 model
            return "products-by-type";
        }
        return "redirect:/products";
    }

    @PostMapping("/products/{productId}/add-type/{typeId}")
    @ResponseBody
    public String addProductToType(@PathVariable Long productId, @PathVariable Long typeId) {
        boolean success = productService.addProductToType(productId, typeId);
        return success ? "success" : "failed";
    }

    @PostMapping("/products/{productId}/remove-type/{typeId}")
    @ResponseBody
    public String removeProductFromType(@PathVariable Long productId, @PathVariable Long typeId) {
        boolean success = productService.removeProductFromType(productId, typeId);
        return success ? "success" : "failed";
    }

    // 添加商品页面
    @GetMapping("/add-product")
    public String showAddProductPage(Model model) {
        List<ProductType> types = productTypeService.getAllTypes();
        model.addAttribute("types", types);
        return "add-product";
    }

    // 添加商品API - 支持同时关联类型
    @PostMapping("/api/products")
    @ResponseBody
    public String addProduct(@RequestBody ProductRequest request) {
        try {
            Product product = new Product();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setImageUrl(request.getImageUrl());

            Product savedProduct = productService.saveProduct(product);
            if (savedProduct != null) {
                // 添加默认的温度和份量属性
                productAttributeService.addAttribute("温度", "多冰", "默认温度", savedProduct);
                productAttributeService.addAttribute("份量", "中", "默认份量", savedProduct);

                // 如果提供了类型ID，将商品添加到对应类型
                if (request.getProductTypeId() != null) {
                    ProductType type = productTypeService.findTypeById(request.getProductTypeId());
                    if (type != null) {
                        productService.addProductToType(savedProduct.getId(), type.getId());
                    }
                }

                return "success";
            } else {
                return "failed";
            }
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // 定义接收请求的类
    public static class ProductRequest {
        private String name;
        private String description;
        private Double price;
        private String imageUrl;
        private Long productTypeId; // 关联的产品类型ID

        // Getter 和 Setter 方法
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

        public Long getProductTypeId() { return productTypeId; }
        public void setProductTypeId(Long productTypeId) { this.productTypeId = productTypeId; }
    }
}
