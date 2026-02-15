package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "kite.rate-limit")
public class KiteRateLimitProperties {

    private boolean enabled = true;
    private int requestsPerSecond = 10;
    private int burstCapacity = 20;
    private List<String> excludedIps = new ArrayList<>();
    private List<String> excludedPaths = new ArrayList<>();
}
