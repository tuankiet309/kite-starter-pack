package com.kite.redis.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作工具类
 * Redis utility class for common cache operations
 */
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    // ========== String Operations ==========

    /**
     * Set value with expiration
     */
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * Set value without expiration
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Get value by key
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Delete key
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * Delete multiple keys
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * Check if key exists
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * Set expiration time
     */
    public Boolean expire(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }

    /**
     * Get expiration time (in seconds)
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    // ========== Hash Operations ==========

    /**
     * Set hash field
     */
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * Get hash field
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * Get all hash entries
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * Set multiple hash fields
     */
    public void hSetAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * Delete hash field
     */
    public Long hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * Check if hash field exists
     */
    public Boolean hHasKey(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * Increment hash field value
     */
    public Long hIncrement(String key, String field, long delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }

    // ========== List Operations ==========

    /**
     * Left push to list
     */
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * Right push to list
     */
    public Long rPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * Left pop from list
     */
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * Right pop from list
     */
    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * Get list range
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * Get list size
     */
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // ========== Set Operations ==========

    /**
     * Add members to set
     */
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * Get all set members
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * Check if member exists in set
     */
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * Get set size
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * Remove members from set
     */
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    // ========== Sorted Set Operations ==========

    /**
     * Add member to sorted set with score
     */
    public Boolean zAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * Get sorted set range by score
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * Increment member score in sorted set
     */
    public Double zIncrementScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * Get member rank in sorted set
     */
    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * Remove member from sorted set
     */
    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    // ========== Increment/Decrement Operations ==========

    /**
     * Increment value by delta
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Decrement value by delta
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }
}
