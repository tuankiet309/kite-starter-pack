package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kite.resources")
public class KiteResourceProperties {

    private String uploadDir = "uploads";
    private long maxFileSize = 10485760; // 10MB
    private String allowedExtensions = "jpg,png,pdf,docx,xlsx";
    private boolean serveStaticContent = true;
    private StorageType type = StorageType.LOCAL;
    private Minio minio = new Minio();

    public enum StorageType {
        LOCAL, MINIO
    }

    @Data
    public static class Minio {
        private String url;
        private String accessKey;
        private String secretKey;
        private String bucket;
    }
}
