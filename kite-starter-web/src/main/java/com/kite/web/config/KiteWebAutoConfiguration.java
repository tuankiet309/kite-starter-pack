package com.kite.web.config;

import com.kite.config.properties.KiteI18nProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Web Auto-configuration
 */
@Configuration
@EnableConfigurationProperties({ KiteI18nProperties.class, com.kite.config.properties.KiteWebProperties.class })
@RequiredArgsConstructor
public class KiteWebAutoConfiguration implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    private final KiteI18nProperties i18nProperties;
    private final com.kite.config.properties.KiteWebProperties webProperties;

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(i18nProperties.getBasename());
        messageSource.setDefaultEncoding(i18nProperties.getEncoding());
        messageSource.setCacheSeconds((int) i18nProperties.getCacheDuration().getSeconds());
        return messageSource;
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(i18nProperties.getDefaultLocale());
        return resolver;
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public com.kite.web.exception.GlobalExceptionHandler globalExceptionHandler() {
        return new com.kite.web.exception.GlobalExceptionHandler();
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public com.kite.web.i18n.MessageUtil messageUtil(MessageSource messageSource) {
        return new com.kite.web.i18n.MessageUtil(messageSource);
    }

    @Override
    public void configurePathMatch(org.springframework.web.servlet.config.annotation.PathMatchConfigurer configurer) {
        if (webProperties.getApi().isEnabled()) {
            String prefix = webProperties.getApi().getFullPrefix();
            configurer.addPathPrefix(prefix,
                    c -> c.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
        }
    }
}
