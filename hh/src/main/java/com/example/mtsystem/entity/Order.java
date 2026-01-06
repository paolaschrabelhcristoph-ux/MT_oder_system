package com.example.mtsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 在Order实体中确保items字段被正确加载
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // 防止序列化时无限递归
    private List<OrderItem> items;


    @Column(nullable = false)
    private Double totalAmount;
    @Column(name = "status")
    private String status = "待支付"; // 默认状态


    @Column(nullable = false)
    private LocalDateTime orderTime;

    // 构造函数
    public Order() {
        this.orderTime = LocalDateTime.now();
        this.status = "待支付";
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }


    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getOrderTime() {
        return orderTime != null ? orderTime : LocalDateTime.now();
    }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }


}
