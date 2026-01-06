package com.example.mtsystem.controller;

import com.example.mtsystem.entity.*;
import com.example.mtsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductAttributeService productAttributeService;

    // 获取用户个人资料
    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProfile(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Map<String, Object> response = new HashMap<>();
        if (currentUser != null) {
            response.put("success", true);
            response.put("user", currentUser);
        } else {
            response.put("success", false);
            response.put("message", "用户未登录");
        }
        return ResponseEntity.ok(response);
    }

    // 更新用户资料
    @PostMapping("/profile/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, String> request, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Map<String, Object> response = new HashMap<>();
        if (currentUser != null) {
            User updatedUser = userService.updateUser(currentUser.getId(), request.get("username"), request.get("email"), currentUser.getAvatarPath());
            if (updatedUser != null) {
                session.setAttribute("currentUser", updatedUser);
                response.put("success", true);
            } else {
                response.put("success", false);
                response.put("message", "更新失败");
            }
        } else {
            response.put("success", false);
            response.put("message", "用户未登录");
        }
        return ResponseEntity.ok(response);
    }

    // 上传头像
    @PostMapping("/profile/upload-avatar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Map<String, Object> response = new HashMap<>();
        if (currentUser != null) {
            try {
                User updatedUser = userService.uploadAvatar(currentUser.getId(), avatarFile);
                if (updatedUser != null) {
                    session.setAttribute("currentUser", updatedUser);
                    response.put("success", true);
                    response.put("avatarPath", updatedUser.getAvatarPath());
                } else {
                    response.put("success", false);
                    response.put("message", "头像上传失败");
                }
            } catch (IOException e) {
                response.put("success", false);
                response.put("message", "头像上传失败：" + e.getMessage());
            }
        } else {
            response.put("success", false);
            response.put("message", "用户未登录");
        }
        return ResponseEntity.ok(response);
    }

    // 修改密码
    @PostMapping("/profile/change-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> request, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Map<String, Object> response = new HashMap<>();
        if (currentUser != null) {
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");
            String confirmNewPassword = request.get("confirmNewPassword");

            if (!newPassword.equals(confirmNewPassword)) {
                response.put("success", false);
                response.put("message", "两次输入的新密码不一致");
            } else {
                User updatedUser = userService.updatePassword(currentUser.getId(), oldPassword, newPassword);
                if (updatedUser != null) {
                    session.setAttribute("currentUser", updatedUser);
                    response.put("success", true);
                    response.put("message", "密码修改成功");
                } else {
                    response.put("success", false);
                    response.put("message", "原密码错误");
                }
            }
        } else {
            response.put("success", false);
            response.put("message", "用户未登录");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCart(HttpSession session) {
        // 强制清除缓存，获取最新数据
        session.removeAttribute("cachedCartData");

        // 从服务层获取最新购物车数据
        List<CartItem> cartItems = cartService.getUserCartItems(session);

        // 可选：重新缓存最新数据
        session.setAttribute("cachedCartData", cartItems);

        Map<String, Object> response = new HashMap<>();
        response.put("cartItems", cartItems);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // 获取所有产品类型
    @GetMapping("/product-types")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductTypes() {
        Map<String, Object> response = new HashMap<>();
        List<ProductType> types = productTypeService.getAllTypes();
        response.put("types", types);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // 获取所有产品
    @GetMapping("/products")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productService.getAllProducts();
        response.put("products", products);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // 获取指定类型的产品
    @GetMapping("/products/type/{typeId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductsByType(@PathVariable Long typeId) {
        Map<String, Object> response = new HashMap<>();
        ProductType type = productTypeService.findTypeById(typeId);
        if (type != null) {
            List<Product> products = productService.getProductsByType(type);
            response.put("products", products);
            response.put("typeName", type.getName());
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "产品类型不存在");
        }
        return ResponseEntity.ok(response);
    }

    // 获取商品的属性
    @GetMapping("/products/{productId}/attributes")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductAttributes(@PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();
        Product product = productService.getProductById(productId);
        if (product != null) {
            List<ProductAttribute> attributes = productAttributeService.getAttributesByProduct(product);
            response.put("attributes", attributes);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "商品不存在");
        }
        return ResponseEntity.ok(response);
    }

    // 添加到购物车
    // 在ApiController.java中修改addToCart方法
    // 添加到购物车
    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request, HttpSession session) {
        Long productId = ((Integer) request.get("productId")).longValue();
        String temperature = (String) request.get("temperature");
        String size = (String) request.get("size");
        String deliveryAddress = (String) request.get("deliveryAddress");

        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = cartService.addToCart(productId, session, temperature, size, deliveryAddress);
            if (success) {
                response.put("success", true);
                response.put("message", "商品已添加到购物车");
            } else {
                response.put("success", false);
                response.put("message", "添加失败");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }



    // 从购物车移除
    // 在ApiController.java中修复removeFromCart方法
    @PostMapping("/cart/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestBody Map<String, Object> request, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.ok(response);
        }

        try {
            Long productId = ((Integer) request.get("productId")).longValue();
            cartService.removeFromCart(productId, session);
            response.put("success", true);
            response.put("message", "商品已移除");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }


    // 结算购物车
    // 在ApiController.java中完善结算功能
    @PostMapping("/cart/checkout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 结算购物车，创建订单
            boolean success = cartService.checkout(session);
            if (success) {
                response.put("success", true);
                response.put("message", "订单创建成功");

                // 清除购物车缓存，确保下次访问购物车是空的
                session.removeAttribute("cachedCartData");
            } else {
                response.put("success", false);
                response.put("message", "订单创建失败");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }


    // 获取用户订单
    // 在ApiController.java中确保getOrders返回完整的订单信息
    @GetMapping("/orders")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getOrders(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Map<String, Object> response = new HashMap<>();

        if (currentUser != null) {
            List<com.example.mtsystem.entity.Order> orders = orderService.getOrdersByUser(currentUser.getId());
            response.put("orders", orders);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "用户未登录");
        }

        return ResponseEntity.ok(response);
    }


    // 在ApiController.java中添加支付功能
    @PostMapping("/order/pay/{orderId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> payOrder(@PathVariable Long orderId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.ok(response);
        }

        try {
            // 更新订单状态为"已支付"
            boolean success = orderService.updateOrderStatus(orderId, "已支付");
            if (success) {
                response.put("success", true);
                response.put("message", "支付成功");
            } else {
                response.put("success", false);
                response.put("message", "订单不存在或无法更新状态");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "支付失败：" + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }


}
