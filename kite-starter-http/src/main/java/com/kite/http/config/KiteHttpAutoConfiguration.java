package com.kite.http.config;

import com.kite.config.properties.KiteHttpClientProperties;
import com.kite.http.client.KiteHttpClient;
import com.kite.http.client.impl.RestTemplateHttpClient;
import com.kite.http.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Auto-configuration for Kite HTTP Client
 */
@Configuration
@EnableConfigurationProperties(KiteHttpClientProperties.class)
@ConditionalOnProperty(prefix = "kite.http-client", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class KiteHttpAutoConfiguration {

    private final KiteHttpClientProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        if (properties.isLogging()) {
            builder = builder.additionalInterceptors(loggingInterceptor());
        }
        return builder
                .setConnectTimeout(properties.getConnectTimeout())
                .setReadTimeout(properties.getReadTimeout())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "kite.http-client", name = "logging", havingValue = "true")
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.springframework.web.reactive.function.client.WebClient")
    @ConditionalOnProperty(prefix = "kite.http-client", name = "logging", havingValue = "true")
    public org.springframework.boot.web.reactive.function.client.WebClientCustomizer loggingWebClientCustomizer() {
        return builder -> builder.filter(new com.kite.http.filter.LoggingExchangeFilterFunction());
    }

    @Bean
    @ConditionalOnMissingBean(KiteHttpClient.class)
    public KiteHttpClient kiteHttpClient(RestTemplate restTemplate) {
        return new RestTemplateHttpClient(restTemplate);
    }
}
