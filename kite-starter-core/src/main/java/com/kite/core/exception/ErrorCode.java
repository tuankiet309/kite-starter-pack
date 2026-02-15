package com.kite.core.exception;

/**
 * Standard interface for error codes
 */
public interface ErrorCode {

    /**
     * Get the error code
     */
    /**
     * Get the error code
     */
    String getCode();

    /**
     * Get the error message
     */
    String getMessage();

    /**
     * Get the HTTP status code value (e.g., 400, 404, 500)
     */
    int getHttpStatus();
}
