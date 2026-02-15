package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "kite.resilience")
public class KiteResilienceProperties {

    private boolean enabled = true;
    private CircuitBreaker circuitBreaker = new CircuitBreaker();
    private Retry retry = new Retry();

    @Data
    public static class CircuitBreaker {
        private float failureRateThreshold = 50.0f;
        private Duration waitDurationInOpenState = Duration.ofSeconds(10);
        private int slidingWindowSize = 10;
        private int minimumNumberOfCalls = 5;
    }

    @Data
    public static class Retry {
        private int maxAttempts = 3;
        private Duration waitDuration = Duration.ofSeconds(2);
    }
}
