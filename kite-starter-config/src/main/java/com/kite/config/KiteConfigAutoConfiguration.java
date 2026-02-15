package com.kite.config;

import com.kite.config.properties.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        KiteSecurityProperties.class,
        KiteDatabaseProperties.class,
        KiteRedisProperties.class,
        KiteI18nProperties.class,
        KiteResilienceProperties.class,
        KiteRateLimitProperties.class,
        KiteHttpClientProperties.class,
        KiteResourceProperties.class,
        KiteWebSocketProperties.class
})
public class KiteConfigAutoConfiguration {
    // This class enables all property classes so they can be injected anywhere
}
