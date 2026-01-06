package com.example.mtsystem.controller;

import com.example.mtsystem.entity.Product;
import com.example.mtsystem.entity.User;
import com.example.mtsystem.service.CartService;
import com.example.mtsystem.service.ProductService;
import com.example.mtsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // 修改购物车显示控制器
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }

        // 使用CartService获取购物车项列表
        List<com.example.mtsystem.entity.CartItem> cartItems = cartService.getUserCartItems(session);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", cartService.calculateTotal(session));
        return "index"; // 修改为返回 index 页面，而不是 cart 页面
    }


    // 修改添加商品到购物车后的重定向
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }

        // 将Long类型的用户ID转换为String类型，或修改CartService方法接受Long类型
        cartService.addToCart(productId, session, String.valueOf(user.getId()), "多冰", "中");
        return "redirect:/index"; // 修改为重定向到 index 页面，而不是 products 页面
    }


    // 修改移除商品后的重定向
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }

        // 使用CartService从购物车移除商品
        cartService.removeFromCart(productId, session);
        return "redirect:/index"; // 修改为重定向到 index 页面，而不是 cart 页面
    }


    // 修改结算后的重定向
    @PostMapping("/cart/checkout")
    public String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }

        // 使用CartService进行结算
        boolean success = cartService.checkout(session);
        if (success) {
            model.addAttribute("message", "订单创建成功");
            return "redirect:/index"; // 修改为重定向到 index 页面，而不是 checkout-success 页面
        } else {
            model.addAttribute("error", "订单创建失败");
            return "redirect:/index"; // 修改为重定向到 index 页面
        }
    }


    // 移除冲突的路径映射，避免与ProductController重复
    // @GetMapping("/products") 方法已在ProductController中定义，无需在此重复定义
}
