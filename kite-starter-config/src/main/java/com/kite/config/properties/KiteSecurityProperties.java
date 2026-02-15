package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * Security Properties
 */
@Data
@ConfigurationProperties(prefix = "kite.security")
public class KiteSecurityProperties {

    private boolean enabled = true;
    private List<String> publicEndpoints;
    private Jwt jwt = new Jwt();
    private Cors cors = new Cors();

    @Data
    public static class Jwt {
        private String secretKey;
        private Duration accessTokenValidity = Duration.ofHours(1);
        private Duration refreshTokenValidity = Duration.ofDays(7);
        private boolean rememberMeEnabled = true;
        private String blacklistPrefix = "kite:auth:blacklist:";
        private String tokenPrefix = "Bearer ";
        private boolean statelessRolesEnabled = false;
        private String roleClaimKey = "roles";
    }

    @Data
    public static class Cors {
        private boolean enabled = true;
        private List<String> allowedOrigins = List.of("*");
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
        private List<String> allowedHeaders = List.of("*");
        private boolean allowCredentials = true;
        private long maxAge = 3600;
    }
}
