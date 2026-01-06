package com.example.mtsystem.repository;

import com.example.mtsystem.entity.CartItem;
import com.example.mtsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 优化查询，避免N+1问题
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product p WHERE ci.user = :user")
    List<CartItem> findByUserOptimized(@Param("user") User user);

    void deleteByUserAndProduct_Id(User user, Long productId);

    // 确保方法正确实现
    @Query("SELECT ci FROM CartItem ci WHERE ci.user = :user")
    List<CartItem> findByUser(@Param("user") User user);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user = :user AND ci.product.id = :productId")
    CartItem findByUserAndProduct_Id(@Param("user") User user, @Param("productId") Long productId);

    void deleteByUser(User user);
}
