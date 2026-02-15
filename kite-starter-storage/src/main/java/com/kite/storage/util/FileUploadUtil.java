package com.kite.storage.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * File upload utility
 * 文件上传工具类
 */
@RequiredArgsConstructor
public class FileUploadUtil {

    private static final String DEFAULT_UPLOAD_DIR = "uploads";

    /**
     * Upload file with original name
     */
    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, DEFAULT_UPLOAD_DIR);
    }

    /**
     * Upload file to specified directory
     */
    public String uploadFile(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Create directory if not exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }

    /**
     * Delete file
     */
    public boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if file exists
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Get file extension
     */
    public String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * Validate file type
     */
    public boolean isValidFileType(String filename, String[] allowedExtensions) {
        String extension = getFileExtension(filename).toLowerCase();
        for (String allowed : allowedExtensions) {
            if (extension.equals(allowed.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
