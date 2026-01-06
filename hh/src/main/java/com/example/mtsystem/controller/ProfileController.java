package com.example.mtsystem.controller;

import com.example.mtsystem.entity.User;
import com.example.mtsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/edit-profile")
    public String showEditProfile(Model model, Principal principal) {
        User user = getCurrentUser(principal);
        if (user == null) {
            // 重定向到登录页面或处理未登录情况
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "edit-profile";
    }


    private User getCurrentUser(Principal principal) {
        if (principal == null) {
            return null; // 或抛出异常
        }
        return userService.findByUsername(principal.getName());
    }

}
