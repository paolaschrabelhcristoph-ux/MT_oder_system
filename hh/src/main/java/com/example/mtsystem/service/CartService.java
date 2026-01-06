package com.example.mtsystem.service;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.User;
import com.example.mtsystem.entity.CartItem;
import com.example.mtsystem.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemRepository cartItemRepository;

    // 购物车数据存储在 session 中的键名
    private static final String CART_KEY = "cart";

    /**
     * 获取购物车内容
     * @param session HTTP会话
     * @return 购物车中的商品及其数量
     */
    public Map<CartItem, Integer> getCart(HttpSession session) {
        Map<CartItem, Integer> cart = (Map<CartItem, Integer>) session.getAttribute(CART_KEY);
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute(CART_KEY, cart);
        }
        return cart;
    }

    /**
     * 将商品添加到购物车
     * @param productId 商品ID
     * @param session HTTP会话
     * @param temperature 温度选项
     * @param size 份量选项
     * @return 添加成功返回true，否则返回false
     */
    public boolean addToCart(Long productId, HttpSession session, String temperature, String size) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return false;
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return false;
        }

        // 检查是否已存在相同商品、温度和份量的购物车项
        CartItem existingItem = cartItemRepository.findByUserAndProduct_Id(user, productId);
        if (existingItem != null &&
                ((existingItem.getTemperature() == null && temperature == null) ||
                        (existingItem.getTemperature() != null && existingItem.getTemperature().equals(temperature))) &&
                ((existingItem.getSize() == null && size == null) ||
                        (existingItem.getSize() != null && existingItem.getSize().equals(size)))) {
            // 如果存在，增加数量
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            // 如果不存在，创建新的购物车项
            CartItem cartItem = new CartItem(user, product, 1, product.getPrice(), temperature, size);
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    /**
     * 从购物车中移除商品
     * @param productId 商品ID
     * @param session HTTP会话
     * @return 移除成功返回true，否则返回false
     */
    public boolean removeFromCart(Long productId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return false;
        }

        cartItemRepository.deleteByUserAndProduct_Id(user, productId);
        return true;
    }

    /**
     * 更新购物车中商品的数量
     * @param productId 商品ID
     * @param quantity 新数量
     * @param session HTTP会话
     * @return 更新成功返回true，否则返回false
     */
    public boolean updateQuantity(Long productId, Integer quantity, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return false;
        }

        CartItem cartItem = cartItemRepository.findByUserAndProduct_Id(user, productId);
        if (cartItem == null) {
            return false;
        }

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    /**
     * 清空购物车
     * @param session HTTP会话
     */
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_KEY);
    }

    /**
     * 计算购物车总金额
     * @param session HTTP会话
     * @return 购物车总金额
     */
    public Double calculateTotal(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return 0.0;
        }

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        Double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    /**
     * 结算购物车
     * @param session HTTP会话
     * @return 结算结果
     */
    public boolean checkout(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return false;
        }

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            return false;
        }

        // 创建订单项列表，避免循环依赖
        java.util.List<com.example.mtsystem.entity.OrderItem> orderItems = new java.util.ArrayList<>();
        for (CartItem cartItem : cartItems) {
            // 直接使用购物车项中的产品和价格，避免再次查询数据库
            com.example.mtsystem.entity.OrderItem orderItem = new com.example.mtsystem.entity.OrderItem(
                    cartItem.getProduct(), cartItem.getQuantity(), cartItem.getPrice());
            orderItems.add(orderItem);
        }

        // 创建订单并保存
        com.example.mtsystem.entity.Order order = orderService.createOrder(orderItems, user);

        // 清空购物车
        cartItemRepository.deleteByUser(user);

        return order != null;
    }

    /**
     * 获取购物车中商品总数
     * @param session HTTP会话
     * @return 商品总数
     */
    public int getCartItemCount(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return 0;
        }

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    /**
     * 获取用户购物车项列表
     * @param session HTTP会话
     * @return 购物车项列表
     */
    public List<CartItem> getUserCartItems(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return java.util.Collections.emptyList();
        }
        return cartItemRepository.findByUser(user);
    }
}
