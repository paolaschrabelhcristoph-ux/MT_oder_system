package com.example.mtsystem.config;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.ProductType;
import com.example.mtsystem.service.ProductService;
import com.example.mtsystem.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataConfig implements CommandLineRunner {

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        // 创建奶茶类型
        ProductType classicMilkTea = productTypeService.createType("经典奶茶", "传统口味的奶茶系列");
        ProductType fruitTea = productTypeService.createType("水果茶", "新鲜水果制作的茶饮");
        ProductType coffee = productTypeService.createType("咖啡系列", "各种咖啡饮品");
        ProductType cheeseTea = productTypeService.createType("奶盖茶", "带有奶盖的特色茶饮");
        ProductType seasonal = productTypeService.createType("季节限定", "根据季节推出的限定产品");

        // 这里可以添加产品到类型的关联关系
        // 例如，如果有产品数据，可以添加关联
    }
}
