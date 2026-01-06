package com.example.mtsystem.service;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.User;
import com.example.mtsystem.entity.CartItem;
import com.example.mtsystem.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    // 在CartService.java中修改addToCart方法
    public boolean addToCart(Long productId, HttpSession session, String temperature, String size, String deliveryAddress) {
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
            existingItem.setDeliveryAddress(deliveryAddress); // 更新配送地址
            cartItemRepository.save(existingItem); // 立即保存到数据库
        } else {
            // 如果不存在，创建新的购物车项
            CartItem cartItem = new CartItem(user, product, 1, product.getPrice(), temperature, size);
            cartItem.setDeliveryAddress(deliveryAddress); // 设置配送地址
            cartItemRepository.save(cartItem); // 立即保存到数据库
        }

        // 清除缓存，强制下次获取时重新查询
        session.removeAttribute("cachedCartItems");

        return true;
    }


    // 在CartService.java中确保removeFromCart方法正确处理
    // 在CartService.java中为removeFromCart方法添加@Transactional注解
    @Transactional
    public boolean removeFromCart(Long productId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return false;
        }

        try {
            cartItemRepository.deleteByUserAndProduct_Id(user, productId);

            // 清除缓存，强制下次获取时重新查询
            session.removeAttribute("cachedCartItems");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Transactional
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
        // 在CartService.java的checkout方法中
        for (CartItem cartItem : cartItems) {
            // 创建订单项，保留购物车项的所有信息
            com.example.mtsystem.entity.OrderItem orderItem = new com.example.mtsystem.entity.OrderItem(
                    cartItem.getProduct(), cartItem.getQuantity(), cartItem.getPrice());
            orderItem.setTemperature(cartItem.getTemperature()); // 保留温度信息
            orderItem.setSize(cartItem.getSize()); // 保留份量信息
            orderItem.setDeliveryAddress(cartItem.getDeliveryAddress()); // 保留配送地址
            orderItems.add(orderItem);
        }


        // 创建订单并保存
        com.example.mtsystem.entity.Order order = orderService.createOrder(orderItems, user);

        // 清空购物车 - 这个操作也需要事务
        cartItemRepository.deleteByUser(user);

        // 清除缓存
        session.removeAttribute("cachedCartItems");

        return order != null;
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

        // 尝试从session中获取缓存的购物车项
        List<CartItem> cachedCartItems = (List<CartItem>) session.getAttribute("cachedCartItems");
        if (cachedCartItems != null) {
            return cachedCartItems;
        }

        // 如果没有缓存，则从数据库获取
        List<CartItem> cartItems = cartItemRepository.findByUserOptimized(user); // 使用优化查询

        // 将结果缓存到session中
        session.setAttribute("cachedCartItems", cartItems);

        return cartItems;
    }
}
