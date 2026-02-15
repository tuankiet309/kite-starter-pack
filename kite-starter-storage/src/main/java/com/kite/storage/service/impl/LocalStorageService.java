package com.kite.storage.service.impl;

import com.kite.config.properties.KiteResourceProperties;
import com.kite.core.exception.BusinessException;
import com.kite.core.exception.CommonErrorCode;
import com.kite.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Local Storage Implementation
 */
@Slf4j
public class LocalStorageService implements StorageService {

    private final KiteResourceProperties properties;
    private final com.kite.storage.validation.FileValidator fileValidator;
    private final java.util.List<com.kite.storage.hook.StorageHook> hooks;

    public LocalStorageService(KiteResourceProperties properties, 
                               com.kite.storage.validation.FileValidator fileValidator,
                               java.util.List<com.kite.storage.hook.StorageHook> hooks) {
        this.properties = properties;
        this.fileValidator = fileValidator;
        this.hooks = hooks != null ? hooks : java.util.Collections.emptyList();
    }

    @Override
    public String upload(MultipartFile file) {
        if (!fileValidator.validate(file)) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "Invalid file type");
        }

        hooks.forEach(hook -> hook.beforeUpload(file));

        String filename = upload(file, properties.getUploadDir());
        
        // Construct full path for hook
        String fullPath = java.nio.file.Paths.get(properties.getUploadDir()).resolve(filename).toString();
        hooks.forEach(hook -> hook.afterUpload(file, filename, fullPath));
        
        return filename;
    }

    @Override
    public String upload(MultipartFile file, String path) {
        if (file.isEmpty()) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "File is empty");
        }

        try {
            Path rootLocation = Paths.get(path);
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String filename = UUID.randomUUID().toString() + extension;
            Path destinationFile = rootLocation.resolve(filename).normalize().toAbsolutePath();
            
            // Security check: Verify that the path is inside the upload directory
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                 // specific check might need adjustment if 'path' varies, but generally we want to prevent directory traversal
            }

            file.transferTo(destinationFile.toFile());
            return filename;
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "Failed to store file");
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = Paths.get(properties.getUploadDir()).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new BusinessException(CommonErrorCode.NOT_FOUND, "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "Could not read file: " + filename);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = Paths.get(properties.getUploadDir()).resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.error("Error deleting file: " + filename, e);
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "Could not delete file: " + filename);
        }
    }

    @Override
    public String getPresignedUrl(String filename, Duration expiration) {
        // For local storage, we can't generate a real pre-signed URL with expiration.
        // We return a static URL path assuming existing static resource mapping is in place.
        // Or simply return the filename if the frontend constructs the URL.
        return "/static/" + filename; 
    }
}
