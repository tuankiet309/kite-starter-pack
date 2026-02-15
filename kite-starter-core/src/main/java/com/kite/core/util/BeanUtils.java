package com.kite.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.Map;

public final class BeanUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private BeanUtils() {
        throw new AssertionError("No instances allowed");
    }

    /**
     * Copy properties from source to target using Jackson.
     * This performs a deep copy.
     */
    @SneakyThrows
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        return MAPPER.convertValue(source, targetClass);
    }

    /**
     * Convert object to Map.
     */
    public static Map<String, Object> toMap(Object source) {
        return MAPPER.convertValue(source, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
        });
    }
}
