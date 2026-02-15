package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web Configuration Properties
 */
@Data
@ConfigurationProperties(prefix = "kite.web")
public class KiteWebProperties {

    /**
     * API Prefix Configuration
     */
    private Api api = new Api();

    @Data
    public static class Api {
        /**
         * Enable API versioning (prefixing)
         */
        private boolean enabled = true;

        /**
         * API base prefix (e.g., /api)
         */
        private String prefix = "/api";

        /**
         * API version (e.g., v1)
         */
        private String version = "v1";

        public String getFullPrefix() {
            String p = prefix.endsWith("/") ? prefix.substring(0, prefix.length() - 1) : prefix;
            String v = version.startsWith("/") ? version.substring(1) : version;
            return p + "/" + v;
        }
    }
}
