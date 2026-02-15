package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "kite.redis")
public class KiteRedisProperties {

    private boolean enabled = true;
    private String host = "localhost";
    private int port = 6379;
    private String password;
    private int database = 0;
    private Duration timeout = Duration.ofSeconds(60);
    private Duration connectTimeout = Duration.ofSeconds(10);

    private Pool pool = new Pool();

    @Data
    public static class Pool {
        private int maxActive = 8;
        private int maxIdle = 8;
        private int minIdle = 0;
        private Duration maxWait = Duration.ofMillis(-1);
    }
}
