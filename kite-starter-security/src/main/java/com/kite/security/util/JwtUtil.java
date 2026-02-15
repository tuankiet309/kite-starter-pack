package com.kite.security.util;

import com.kite.config.properties.KiteSecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Utility for token generation and validation
 */
@RequiredArgsConstructor
public class JwtUtil {

    private final KiteSecurityProperties properties;

    /**
     * Generate JWT token
     */
    public String generateToken(String username, Map<String, Object> claims, long expirationMs) {
        return createToken(claims, username, expirationMs);
    }

    /**
     * Generate access token (short-lived)
     */
    public String generateAccessToken(String username, Map<String, Object> claims) {
        return createToken(claims, username, 3600000); // 1 hour
    }

    /**
     * Generate refresh token (long-lived)
     */
    public String generateRefreshToken(String username) {
        return createToken(Map.of(), username, 604800000); // 7 days
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract custom claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Check if token is expired
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate token
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getJwt().getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
