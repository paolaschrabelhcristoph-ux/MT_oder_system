package com.example.mtsystem.entity;

import javax.persistence.*;

@Entity
@Table(name = "product_attributes")
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 属性名称，如"温度"、"份量"

    @Column(nullable = false)
    private String value; // 属性值，如"多冰"、"中杯"

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // 构造函数
    public ProductAttribute() {}

    public ProductAttribute(String name, String value, String description, Product product) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.product = product;
    }

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
