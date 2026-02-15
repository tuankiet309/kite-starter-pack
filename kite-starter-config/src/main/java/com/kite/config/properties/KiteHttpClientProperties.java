package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "kite.http-client")
public class KiteHttpClientProperties {

    private Duration connectTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(10);
    private int maxConnections = 100;
    private int maxConnectionsPerRoute = 20;
    private boolean logging = false;
}
