package com.kite.data.spec;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Utility class for creating JPA Specifications
 */
@SuppressWarnings("unchecked")
public class BaseSpecification {

    public static <T> Specification<T> equal(String attribute, Object value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get(attribute), value);
    }

    public static <T> Specification<T> like(String attribute, String value) {
        return (root, query, cb) -> value == null ? null : cb.like(root.get(attribute), "%" + value + "%");
    }

    public static <T> Specification<T> in(String attribute, Collection<?> values) {
        return (root, query, cb) -> (values == null || values.isEmpty()) ? null : root.get(attribute).in(values);
    }

    public static <T> Specification<T> greaterThan(String attribute, Comparable value) {
        return (root, query, cb) -> value == null ? null : cb.greaterThan(root.get(attribute), value);
    }

    public static <T> Specification<T> lessThan(String attribute, Comparable value) {
        return (root, query, cb) -> value == null ? null : cb.lessThan(root.get(attribute), value);
    }

    public static <T> Specification<T> between(String attribute, Comparable min, Comparable max) {
        return (root, query, cb) -> (min == null || max == null) ? null : cb.between(root.get(attribute), min, max);
    }

    public static <T> Specification<T> isTrue(String attribute) {
        return (root, query, cb) -> cb.isTrue(root.get(attribute));
    }

    public static <T> Specification<T> isFalse(String attribute) {
        return (root, query, cb) -> cb.isFalse(root.get(attribute));
    }
}
