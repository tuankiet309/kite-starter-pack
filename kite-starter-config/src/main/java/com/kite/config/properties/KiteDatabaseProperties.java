package com.kite.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;

@Data
@Validated
@ConfigurationProperties(prefix = "kite.database")
public class KiteDatabaseProperties {

    private Hikari hikari = new Hikari();

    @Data
    public static class Hikari {

        private String poolName = "KiteHikariPool";

        @Min(1)
        private int maximumPoolSize = 10;

        @Min(0)
        private int minimumIdle = 5;

        private Duration connectionTimeout = Duration.ofSeconds(30);

        private Duration idleTimeout = Duration.ofMinutes(10);

        private Duration maxLifetime = Duration.ofMinutes(25);

        private Duration leakDetectionThreshold;

        private boolean autoCommit = true;
    }
}
