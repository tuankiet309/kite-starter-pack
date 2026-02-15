package com.kite.storage.service;

import java.io.InputStream;
import java.time.Duration;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Common Storage Service Interface
 */
public interface StorageService {

    /**
     * Upload file
     */
    String upload(MultipartFile file);

    /**
     * Upload file with specific path
     */
    String upload(MultipartFile file, String path);

    /**
     * Load file as Resource
     */
    Resource loadAsResource(String filename);

    /**
     * Delete file
     */
    void delete(String filename);

    /**
     * Generate pre-signed URL (for download/access)
     */
    String getPresignedUrl(String filename, Duration expiration);
}
