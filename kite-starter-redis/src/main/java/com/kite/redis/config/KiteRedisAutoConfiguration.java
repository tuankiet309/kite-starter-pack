package com.kite.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.kite.config.properties.KiteRedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis Auto-configuration
 */
@Configuration
@EnableConfigurationProperties(KiteRedisProperties.class)
@ConditionalOnProperty(prefix = "kite.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class KiteRedisAutoConfiguration {

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // ... (rest of the method remains valid, just inserting before it)
        template.setConnectionFactory(connectionFactory);

        // Use Jackson2JsonRedisSerializer for value serialization
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // Use StringRedisSerializer for key serialization
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // Set key serializer
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        // Set value serializer
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public com.kite.redis.util.RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate) {
        return new com.kite.redis.util.RedisUtil(redisTemplate);
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public com.kite.redis.aspect.DistributedLockAspect distributedLockAspect(org.springframework.data.redis.core.StringRedisTemplate stringRedisTemplate) {
        return new com.kite.redis.aspect.DistributedLockAspect(stringRedisTemplate);
    }
}
