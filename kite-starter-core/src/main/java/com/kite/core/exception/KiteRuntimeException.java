package com.kite.core.exception;

import lombok.Getter;

@Getter
public class KiteRuntimeException extends RuntimeException {

    private final String errorKey;
    private final Object[] args;

    public KiteRuntimeException(String errorKey, Object... args) {
        super(errorKey);
        this.errorKey = errorKey;
        this.args = args;
    }

    public KiteRuntimeException(String errorKey, String message, Object... args) {
        super(message);
        this.errorKey = errorKey;
        this.args = args;
    }

    public KiteRuntimeException(String errorKey, Throwable cause, Object... args) {
        super(errorKey, cause);
        this.errorKey = errorKey;
        this.args = args;
    }
}
