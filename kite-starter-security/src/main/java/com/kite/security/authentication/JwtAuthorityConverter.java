package com.kite.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

/**
 * Strategy interface for converting a JWT token into a collection of granted
 * authorities.
 * Implement this interface to provide custom role/permission logic.
 */
@FunctionalInterface
public interface JwtAuthorityConverter {
    Collection<? extends GrantedAuthority> convert(String token);
}
