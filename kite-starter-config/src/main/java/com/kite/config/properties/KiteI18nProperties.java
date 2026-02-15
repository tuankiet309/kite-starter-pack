package com.kite.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;

@Data
@ConfigurationProperties(prefix = "kite.i18n")
public class KiteI18nProperties {

    private boolean enabled = true;
    private String basename = "messages";
    private String encoding = StandardCharsets.UTF_8.name();
    private Duration cacheDuration = Duration.ofHours(1);
    private Locale defaultLocale = Locale.US;
    private boolean fallbackToSystemLocale = true;
}
