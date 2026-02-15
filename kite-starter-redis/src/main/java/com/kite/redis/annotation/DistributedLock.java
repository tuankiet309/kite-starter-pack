package com.kite.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Distributed Lock Annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * Lock key (SpEL supported)
     */
    String key();

    /**
     * Lock expiration time
     */
    long expire() default 30;

    /**
     * Time unit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * Wait time for acquiring lock (0 means fail fast)
     */
    long waitTime() default 0;
}
