package com.kite.core.exception;

import lombok.Getter;

@Getter
public class BusinessException extends KiteRuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message, Object... args) {
        super(null, message, args); // errorKey null
        this.errorCode = errorCode;
    }
}
