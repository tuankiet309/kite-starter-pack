package com.kite.security.authentication;

import com.kite.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of JwtAuthorityConverter.
 * Extracts roles using the configured claim key and converts them to
 * SimpleGrantedAuthority.
 */
@RequiredArgsConstructor
public class DefaultJwtAuthorityConverter implements JwtAuthorityConverter {

    private final JwtUtil jwtUtil;

    @Override
    public Collection<? extends GrantedAuthority> convert(String token) {
        List<String> roles = jwtUtil.extractRoles(token);
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
