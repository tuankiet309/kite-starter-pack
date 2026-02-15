package com.kite.security.service.impl;

import com.kite.config.properties.KiteSecurityProperties;
import com.kite.redis.util.RedisUtil;
import com.kite.security.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class RedisTokenBlacklistService implements TokenBlacklistService {

    private final RedisUtil redisUtil;
    private final KiteSecurityProperties properties;

    @Override
    public void blacklistToken(String token, long expirationMs) {
        String prefix = properties.getJwt().getBlacklistPrefix();
        String key = prefix + token;
        redisUtil.set(key, "true", Duration.ofMillis(expirationMs));
    }

    @Override
    public boolean isBlacklisted(String token) {
        String prefix = properties.getJwt().getBlacklistPrefix();
        String key = prefix + token;
        return redisUtil.hasKey(key);
    }
}
