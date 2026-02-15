package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "kite.security")
public class KiteSecurityProperties {

    private boolean enabled = true;
    private List<String> publicEndpoints = List.of("/auth/**", "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**");
    private Jwt jwt = new Jwt();
    private Cors cors = new Cors();

    @Data
    public static class Jwt {
        private String secretKey;
        private Duration accessTokenValidity = Duration.ofHours(1);
        private Duration refreshTokenValidity = Duration.ofDays(7);
        private boolean rememberMeEnabled = true;
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
