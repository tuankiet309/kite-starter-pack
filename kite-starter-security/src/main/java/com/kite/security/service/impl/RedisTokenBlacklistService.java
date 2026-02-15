package com.kite.security.service.impl;

import com.kite.security.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@RequiredArgsConstructor
public class RedisTokenBlacklistService implements TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "kite:auth:blacklist:";

    @Override
    public void blacklistToken(String token, long expirationMs) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "true", Duration.ofMillis(expirationMs));
    }

    @Override
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
