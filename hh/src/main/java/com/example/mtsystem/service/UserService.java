package com.example.mtsystem.service;

import com.example.mtsystem.entity.User;
import com.example.mtsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUploadService fileUploadService;

    public boolean registerUser(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            return false;
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(email)) {
            return false;
        }

        // 创建新用户（不加密密码）
        User user = new User(username, password, email);
        userRepository.save(user);
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 更新最后登录时间
            user.setLastLoginTime(java.time.LocalDateTime.now());
            userRepository.save(user);
            return user.getPassword().equals(password); // 直接比较明文密码
        }

        return false;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // 添加这个方法以解决 "无法解析方法 findByUsername" 的错误
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long userId, String username, String email, String avatarPath) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(username);
            user.setEmail(email);
            if (avatarPath != null) {
                user.setAvatarPath(avatarPath);
            }
            user.setUpdatedAt(java.time.LocalDateTime.now());
            return userRepository.save(user);
        }
        return null;
    }

    public User updatePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(oldPassword)) { // 直接比较明文密码
                user.setPassword(newPassword);
                user.setUpdatedAt(java.time.LocalDateTime.now());
                return userRepository.save(user);
            }
        }
        return null;
    }

    // 上传头像
    // 上传头像
    // 上传头像
    public User uploadAvatar(Long userId, MultipartFile avatarFile) throws IOException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 上传文件并获取路径
            String avatarPath = fileUploadService.uploadAvatar(avatarFile);
            if (avatarPath != null) {
                // 保存新路径到用户对象
                user.setAvatarPath(avatarPath);
                user.setUpdatedAt(java.time.LocalDateTime.now());

                // 保存到数据库
                User savedUser = userRepository.save(user);

                return savedUser;
            }
        }
        return null;
    }

}
