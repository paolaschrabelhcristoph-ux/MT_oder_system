package com.example.mtsystem.controller;

import com.example.mtsystem.entity.User;
import com.example.mtsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // 显示登录页面
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // 在 UserController.java 中修改登录成功后的重定向
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        if (userService.authenticateUser(username, password)) {
            User user = userService.findUserByUsername(username);
            model.addAttribute("success", "登录成功！");
            session.setAttribute("currentUser", user);
            return "redirect:/index"; // 修改为跳转到 index 页面，而不是 products 页面
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }


    // 显示注册页面
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // 处理注册请求
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {

        if (userService.registerUser(username, password, email)) {
            model.addAttribute("success", "注册成功，请登录");
            return "redirect:/login";
        } else {
            model.addAttribute("error", "用户名或邮箱已存在");
            return "register";
        }
    }

    // 仪表板页面（保持不变）
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "dashboard";
    }

    @GetMapping("/profile/edit")
    public String showEditProfile(HttpSession session, Model model, Principal principal) {
        // 首先检查 session 中是否有当前用户
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
            return "edit-profile";
        }

        // 如果 session 中没有用户，尝试通过 Principal 获取
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            if (user != null) {
                model.addAttribute("user", user);
                return "edit-profile";
            }
        }

        // 如果都没有，则重定向到登录页面
        return "redirect:/login";
    }


    // 更新用户资料
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam(required = false) String avatarUrl,
                                HttpSession session,
                                Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        User updatedUser = userService.updateUser(currentUser.getId(), username, email, avatarUrl);
        session.setAttribute("currentUser", updatedUser);
        model.addAttribute("user", updatedUser);
        model.addAttribute("success", "资料更新成功");
        return "edit-profile";
    }

    // 上传头像
    // 上传头像
    // 上传头像
    @PostMapping("/profile/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile,
                               HttpSession session,
                               Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            // 验证文件类型
            String contentType = avatarFile.getContentType();
            if (contentType != null && !contentType.startsWith("image/")) {
                model.addAttribute("error", "请选择图片文件");
                model.addAttribute("user", currentUser);
                return "edit-profile";
            }

            // 验证文件大小（限制为5MB）
            if (avatarFile.getSize() > 5 * 1024 * 1024) {
                model.addAttribute("error", "图片文件大小不能超过5MB");
                model.addAttribute("user", currentUser);
                return "edit-profile";
            }

            User updatedUser = userService.uploadAvatar(currentUser.getId(), avatarFile);
            if (updatedUser != null) {
                // 更新session中的用户信息
                session.setAttribute("currentUser", updatedUser);
                model.addAttribute("user", updatedUser);
                model.addAttribute("success", "头像上传成功");
            } else {
                model.addAttribute("error", "头像上传失败");
                model.addAttribute("user", currentUser);
            }
        } catch (IOException e) {
            model.addAttribute("error", "头像上传失败：" + e.getMessage());
            model.addAttribute("user", currentUser);
        }

        return "edit-profile";
    }


    // 修改密码页面
    @GetMapping("/profile/change-password")
    public String showChangePassword(Model model) {
        return "change-password";
    }

    // 更新密码
    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 HttpSession session,
                                 Model model) {
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "两次输入的新密码不一致");
            return "change-password";
        }

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        User updatedUser = userService.updatePassword(currentUser.getId(), oldPassword, newPassword);
        if (updatedUser != null) {
            model.addAttribute("success", "密码修改成功");
        } else {
            model.addAttribute("error", "原密码错误");
        }

        return "change-password";
    }

    // 登出
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
