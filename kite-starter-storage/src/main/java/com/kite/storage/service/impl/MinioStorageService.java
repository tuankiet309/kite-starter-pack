package com.kite.storage.service.impl;

import com.kite.config.properties.KiteResourceProperties;
import com.kite.core.exception.BusinessException;
import com.kite.core.exception.CommonErrorCode;
import com.kite.storage.service.StorageService;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * MinIO Storage Implementation
 */
@Slf4j
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;
    private final KiteResourceProperties properties;
    private final com.kite.storage.validation.FileValidator fileValidator;
    private final java.util.List<com.kite.storage.hook.StorageHook> hooks;

    public MinioStorageService(MinioClient minioClient, 
                               KiteResourceProperties properties, 
                               com.kite.storage.validation.FileValidator fileValidator,
                               java.util.List<com.kite.storage.hook.StorageHook> hooks) {
        this.minioClient = minioClient;
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
        
        String filename = upload(file, properties.getMinio().getBucket());
        
        hooks.forEach(hook -> hook.afterUpload(file, filename, properties.getMinio().getBucket() + "/" + filename));
        
        return filename;
    }

    @Override
    public String upload(MultipartFile file, String bucket) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // Check if bucket exists
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return filename;
        } catch (Exception e) {
            log.error("MinIO upload failed", e);
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "MinIO upload failed");
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(properties.getMinio().getBucket())
                            .object(filename)
                            .build());
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND, "File not found: " + filename);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(properties.getMinio().getBucket())
                            .object(filename)
                            .build());
        } catch (Exception e) {
            log.error("MinIO delete failed", e);
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "MinIO delete failed");
        }
    }

    @Override
    public String getPresignedUrl(String filename, Duration expiration) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(properties.getMinio().getBucket())
                            .object(filename)
                            .expiry((int) expiration.getSeconds(), TimeUnit.SECONDS)
                            .build());
        } catch (Exception e) {
            log.error("Failed to generate presigned URL", e);
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "Failed to generate URL");
        }
    }
}
