package com.kite.security.service;

public interface TokenBlacklistService {

    /**
     * Blacklist a token
     * 
     * @param token        the JWT token string
     * @param expirationMs remaining time in milliseconds
     */
    void blacklistToken(String token, long expirationMs);

    /**
     * Check if a token is blacklisted
     * 
     * @param token the JWT token string
     * @return true if blacklisted
     */
    boolean isBlacklisted(String token);
}
