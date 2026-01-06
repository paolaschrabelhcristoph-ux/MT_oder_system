package com.example.mtsystem.service;

import com.example.mtsystem.entity.Order;
import com.example.mtsystem.entity.OrderItem;
import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.User;
import com.example.mtsystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Transactional
    public Order createOrder(List<OrderItem> items, User user) {
        Order order = new Order();
        order.setUser(user);

        Double totalAmount = 0.0;
        for (OrderItem item : items) {
            // 使用购物车项中已有的产品和价格信息，避免重复查询数据库
            Product product = item.getProduct();
            if (product != null) {
                // 使用购物车项中已有的价格，而不是重新查询产品价格
                // 保持购物车项中的价格不变
                item.setPrice(item.getPrice());
                totalAmount += item.getPrice() * item.getQuantity();
                item.setOrder(order); // 设置订单项与订单的关联
            }
        }
        order.setTotalAmount(totalAmount);
        order.setItems(items);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }
}
