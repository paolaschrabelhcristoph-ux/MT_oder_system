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
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // 在OrderService.java中完善订单创建
    // 修改OrderService.java中的createOrder方法
    // 在OrderService.java中确保createOrder方法有@Transactional注解
    // 在OrderService.java中确保订单保留购物车项的所有信息
    @Transactional
    public Order createOrder(List<OrderItem> items, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus("已支付"); // 或根据业务需求设置状态
        order.setOrderTime(java.time.LocalDateTime.now());

        Double totalAmount = 0.0;
        for (OrderItem item : items) {
            Product product = item.getProduct();
            if (product != null) {
                // 保留购物车项中的价格，防止商品价格变动影响历史订单
                item.setPrice(item.getPrice());
                totalAmount += item.getPrice() * item.getQuantity();
                item.setOrder(order); // 建立订单项与订单的关联
            }
        }
        order.setTotalAmount(totalAmount);
        order.setItems(items);
        return orderRepository.save(order);
    }


    // 在OrderService.java中添加状态更新方法
    public boolean updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(newStatus);
            order.setOrderTime(java.time.LocalDateTime.now()); // 更新订单时间
            orderRepository.save(order);
            return true;
        }
        return false;
    }



    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }
}
