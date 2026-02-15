package com.kite.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    // 400 Bad Request
    BAD_REQUEST(400, "Bad Request"),
    INVALID_PARAMETER(400, "Invalid Parameter"),
    MISSING_PARAMETER(400, "Missing Parameter"),

    // 401 Unauthorized
    UNAUTHORIZED(401, "Unauthorized"),
    TOKEN_EXPIRED(401, "Token Expired"),
    TOKEN_INVALID(401, "Token Invalid"),

    // 403 Forbidden
    FORBIDDEN(403, "Forbidden"),
    ACCESS_DENIED(403, "Access Denied"),

    // 404 Not Found
    NOT_FOUND(404, "Not Found"),
    RESOURCE_NOT_FOUND(404, "Resource Not Found"),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    private final int httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
