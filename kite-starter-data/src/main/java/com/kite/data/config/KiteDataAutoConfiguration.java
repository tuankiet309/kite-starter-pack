package com.kite.data.config;

import com.kite.config.properties.KiteDatabaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Data Auto-configuration
 */
@Configuration
@EnableConfigurationProperties(KiteDatabaseProperties.class)
@ConditionalOnProperty(prefix = "kite.database", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableJpaAuditing
@RequiredArgsConstructor
public class KiteDataAutoConfiguration {
    // JPA Auditing enabled
}
