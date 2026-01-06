package com.example.mtsystem.entity;

import javax.persistence.*;

@Entity
@Table(name = "product_type_mappings")
public class ProductTypeMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private ProductType type;

    // 构造函数
    public ProductTypeMapping() {}

    public ProductTypeMapping(Product product, ProductType type) {
        this.product = product;
        this.type = type;
    }

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }
}
