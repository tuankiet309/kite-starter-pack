package com.kite.redis.exception;

import com.kite.core.exception.KiteRuntimeException;

/**
 * Lock acquisition exception
 */
public class LockException extends KiteRuntimeException {

    public LockException(String message) {
        super(message);
    }
}
