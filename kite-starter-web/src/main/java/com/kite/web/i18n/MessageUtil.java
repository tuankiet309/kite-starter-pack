package com.kite.web.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * I18n message utility
 * 国际化消息工具类
 */
@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    /**
     * Get message by code
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * Get message by code with parameters
     */
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * Get message by code, parameters, and locale
     */
    public String getMessage(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * Get message with default message
     */
    public String getMessage(String code, Object[] args, String defaultMessage) {
        return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }
}
