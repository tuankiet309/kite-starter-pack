package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kite.websocket")
public class KiteWebSocketProperties {

    private boolean enabled = false;
    private String endpoint = "/ws";
    private String allowedOrigins = "*";
    private boolean withSockJs = true;
}
