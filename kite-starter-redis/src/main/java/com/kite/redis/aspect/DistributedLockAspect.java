package com.kite.redis.aspect;

import com.kite.redis.annotation.DistributedLock;
import com.kite.redis.exception.LockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Aspect for Distributed Lock
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final StringRedisTemplate redisTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    // Lua script for releasing lock safely
    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = parseKey(distributedLock.key(), joinPoint);
        String uuid = UUID.randomUUID().toString();
        long expire = distributedLock.expire();
        TimeUnit timeUnit = distributedLock.timeUnit();
        long waitTime = distributedLock.waitTime();

        boolean locked = false;
        try {
            if (waitTime > 0) {
                locked = tryLockWithWait(key, uuid, expire, timeUnit, waitTime);
            } else {
                locked = tryLock(key, uuid, expire, timeUnit);
            }

            if (!locked) {
                throw new LockException("Could not acquire lock for key: " + key);
            }

            return joinPoint.proceed();
        } finally {
            if (locked) {
                releaseLock(key, uuid);
            }
        }
    }

    private boolean tryLock(String key, String value, long expire, TimeUnit timeUnit) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, expire, timeUnit);
        return Boolean.TRUE.equals(result);
    }

    private boolean tryLockWithWait(String key, String value, long expire, TimeUnit timeUnit, long waitTime) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeUnit.toMillis(waitTime); // waitTime is in annotation's timeUnit? No, waitTime in annot probably implies specific unit? 
        // Annotation param says: long waitTime() default 0; It shares timeUnit() default SECONDS.
        // So waitTime is in 'timeUnit'.
        
        long waitTimeMillis = timeUnit.toMillis(waitTime);
        long deadline = startTime + waitTimeMillis;

        while (System.currentTimeMillis() < deadline) {
            if (tryLock(key, value, expire, timeUnit)) {
                return true;
            }
            try {
                Thread.sleep(100); // Retry every 100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    private void releaseLock(String key, String value) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT, Long.class);
        redisTemplate.execute(redisScript, Collections.singletonList(key), value);
    }

    private String parseKey(String key, ProceedingJoinPoint joinPoint) {
        if (!StringUtils.hasText(key)) {
            return key;
        }
        
        // If it's a raw string, return it. If it starts with # or contains #, treat as SpEL
        if (!key.contains("#")) {
            return key;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }

        try {
            Expression expression = parser.parseExpression(key);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : key;
        } catch (Exception e) {
            log.error("Failed to parse SpEL key: {}", key, e);
            return key;
        }
    }
}
