package com.kite.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kite.core.exception.KiteRuntimeException;

import java.io.IOException;
import java.util.List;

public class JsonUtils {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private JsonUtils() {
        throw new AssertionError("No instances allowed");
    }

    // Serialize object to JSON string
    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new KiteRuntimeException("JSON write error", e);
        }
    }

    // Serialize object to pretty JSON string
    public static String toPrettyJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new KiteRuntimeException("JSON write error", e);
        }
    }

    // Deserialize JSON string to object
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new KiteRuntimeException("JSON read error", e);
        }
    }

    // Deserialize JSON string to object with TypeReference
    public static <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new KiteRuntimeException("JSON read error", e);
        }
    }

    // Deserialize JSON string to List
    public static <T> List<T> toList(String json, Class<T> clazz) {
        CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new KiteRuntimeException("JSON read error", e);
        }
    }

    // Read JSON string to JsonNode
    public static JsonNode readTree(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (IOException e) {
            throw new KiteRuntimeException("JSON readTree error", e);
        }
    }

    // Convert object to JsonNode
    public static JsonNode toTree(Object obj) {
        return MAPPER.valueToTree(obj);
    }

    // Update target object with JSON patch
    public static <T> T merge(String jsonPatch, T target) {
        try {
            return MAPPER.readerForUpdating(target).readValue(jsonPatch);
        } catch (IOException e) {
            throw new KiteRuntimeException("JSON merge error", e);
        }
    }

    // Convert object to another type
    public static <T> T convert(Object source, Class<T> targetClass) {
        return MAPPER.convertValue(source, targetClass);
    }

    // Deep copy object
    public static <T> T deepCopy(T object, Class<T> clazz) {
        return fromJson(toJson(object), clazz);
    }

    // Safe deserialize (return null on error)
    public static <T> T fromJsonOrNull(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
