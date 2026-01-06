package com.example.mtsystem.repository;

import com.example.mtsystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 添加根据用户ID查询订单的方法，并使用JOIN FETCH优化
    @Query("SELECT o FROM Order o JOIN FETCH o.items JOIN FETCH o.user WHERE o.user.id = :userId")
    List<Order> findByUser_Id(@Param("userId") Long userId);
}
