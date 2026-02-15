package com.kite.storage.validation;

import com.kite.config.properties.KiteResourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * File Validator
 */
@Slf4j
@RequiredArgsConstructor
public class FileValidator {

    private final KiteResourceProperties properties;

    /**
     * Validate file
     */
    public boolean validate(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }

        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            return false;
        }

        String extension = getExtension(filename);
        if (!StringUtils.hasText(extension)) {
            return false;
        }

        String allowedExtensions = properties.getAllowedExtensions(); // e.g., "jpg,png,pdf"
        if (!StringUtils.hasText(allowedExtensions)) {
            return true; // No restrictions
        }

        List<String> allowedList = Arrays.asList(allowedExtensions.toLowerCase().split(","));
        return allowedList.contains(extension.toLowerCase());
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }
}
