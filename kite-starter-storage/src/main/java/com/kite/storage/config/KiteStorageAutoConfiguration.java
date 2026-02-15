package com.kite.storage.config;

import com.kite.config.properties.KiteResourceProperties;
import lombok.RequiredArgsConstructor;
import com.kite.storage.service.StorageService;
import com.kite.storage.service.impl.LocalStorageService;
import com.kite.storage.service.impl.MinioStorageService;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Storage Auto-configuration
 */
@Configuration
@EnableConfigurationProperties(KiteResourceProperties.class)
@ConditionalOnProperty(prefix = "kite.resource", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class KiteStorageAutoConfiguration {
    private final KiteResourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public com.kite.storage.validation.FileValidator fileValidator() {
        return new com.kite.storage.validation.FileValidator(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public com.kite.storage.util.FileUploadUtil fileUploadUtil() {
        return new com.kite.storage.util.FileUploadUtil();
    }

    @Bean
    @ConditionalOnMissingBean(StorageService.class)
    @ConditionalOnProperty(prefix = "kite.resources", name = "type", havingValue = "LOCAL", matchIfMissing = true)
    public StorageService localStorageService(com.kite.storage.validation.FileValidator fileValidator, java.util.List<com.kite.storage.hook.StorageHook> hooks) {
        return new LocalStorageService(properties, fileValidator, hooks);
    }

    @Bean
    @ConditionalOnMissingBean(StorageService.class)
    @ConditionalOnProperty(prefix = "kite.resources", name = "type", havingValue = "MINIO")
    public StorageService minioStorageService(MinioClient minioClient, com.kite.storage.validation.FileValidator fileValidator, java.util.List<com.kite.storage.hook.StorageHook> hooks) {
        return new MinioStorageService(minioClient, properties, fileValidator, hooks);
    }

    @Bean
    @ConditionalOnProperty(prefix = "kite.resources", name = "type", havingValue = "MINIO")
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(properties.getMinio().getUrl())
                .credentials(properties.getMinio().getAccessKey(), properties.getMinio().getSecretKey())
                .build();
    }

    @Configuration
    @ConditionalOnProperty(prefix = "kite.resources", name = "serve-static-content", havingValue = "true", matchIfMissing = true)
    public static class StorageWebConfiguration implements WebMvcConfigurer {
        
        @Autowired
        private KiteResourceProperties properties;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String uploadDir = properties.getUploadDir();
            Path path = Paths.get(uploadDir);
            String absolutePath = path.toAbsolutePath().toUri().toString();
            
            registry.addResourceHandler("/static/**")
                    .addResourceLocations(absolutePath);
        }
    }
}
