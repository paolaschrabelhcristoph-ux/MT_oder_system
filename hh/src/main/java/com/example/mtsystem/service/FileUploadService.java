package com.example.mtsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    // 使用相对路径，确保在项目根目录下创建上传目录
    private final String UPLOAD_DIR = "uploads/avatars/";

    public String uploadAvatar(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType != null && !contentType.startsWith("image/")) {
            throw new IOException("不支持的文件类型: " + contentType);
        }

        // 获取项目根目录路径
        String realPath = System.getProperty("user.dir");
        if (realPath == null) {
            // 如果获取不到项目根目录，则使用相对路径
            realPath = "";
        }

        // 构建完整的上传路径
        Path uploadPath = Paths.get(realPath, UPLOAD_DIR);

        // 创建上传目录（包括所有必要的父目录）
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName != null ?
                originalFileName.substring(originalFileName.lastIndexOf('.')) : ".jpg";
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // 构建完整文件路径
        Path filePath = uploadPath.resolve(fileName);

        // 保存文件
        file.transferTo(filePath.toFile());

        // 返回相对路径，供数据库存储和前端访问
        return "/uploads/avatars/" + fileName;
    }
}

