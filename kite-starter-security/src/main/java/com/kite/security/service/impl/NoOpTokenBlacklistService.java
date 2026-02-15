package com.kite.security.service.impl;

import com.kite.security.service.TokenBlacklistService;

public class NoOpTokenBlacklistService implements TokenBlacklistService {

    @Override
    public void blacklistToken(String token, long expirationMs) {
        // No-op
    }

    @Override
    public boolean isBlacklisted(String token) {
        return false;
    }
}
