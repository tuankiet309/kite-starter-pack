package com.kite.security.config;

import com.kite.config.properties.KiteSecurityProperties;
import com.kite.security.filter.JwtAuthenticationFilter;
import com.kite.security.service.TokenBlacklistService;
import com.kite.security.service.impl.NoOpTokenBlacklistService;
import com.kite.security.service.impl.RedisTokenBlacklistService;
import com.kite.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Security Auto-configuration
 */
@Configuration
@EnableConfigurationProperties(KiteSecurityProperties.class)
@ConditionalOnProperty(prefix = "kite.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class KiteSecurityAutoConfiguration {

    private final KiteSecurityProperties properties;

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnBean(StringRedisTemplate.class)
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(TokenBlacklistService.class)
    public TokenBlacklistService redisTokenBlacklistService(StringRedisTemplate redisTemplate) {
        return new RedisTokenBlacklistService(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(TokenBlacklistService.class)
    public TokenBlacklistService noOpTokenBlacklistService() {
        return new NoOpTokenBlacklistService();
    }

    @Bean
    @ConditionalOnBean(UserDetailsService.class)
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService,
            TokenBlacklistService tokenBlacklistService) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService, tokenBlacklistService);
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            JwtAuthenticationFilter jwtAuthFilter) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Public endpoints
                    if (properties.getPublicEndpoints() != null && !properties.getPublicEndpoints().isEmpty()) {
                        auth.requestMatchers(properties.getPublicEndpoints().toArray(String[]::new)).permitAll();
                    }
                    // All other requests require authentication
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        if (properties.getCors().isEnabled()) {
            config.setAllowedOrigins(properties.getCors().getAllowedOrigins());
            config.setAllowedMethods(properties.getCors().getAllowedMethods());
            config.setAllowedHeaders(properties.getCors().getAllowedHeaders());
            config.setAllowCredentials(properties.getCors().isAllowCredentials());
            config.setMaxAge(properties.getCors().getMaxAge());
            source.registerCorsConfiguration("/**", config);
        }
        return source;
    }
}
