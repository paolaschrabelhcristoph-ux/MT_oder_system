package com.example.mtsystem.repository;

import com.example.mtsystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 添加根据用户ID查询订单的方法
    List<Order> findByUser_Id(Long userId);

}
