package com.kite.web.exception;

import com.kite.core.exception.BusinessException;
import com.kite.core.exception.CommonErrorCode;
import com.kite.core.exception.ErrorCode;
import com.kite.web.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("BusinessException: {}", e.getMessage());
        return buildResponse(e.getErrorCode(), e.getMessage(), request.getRequestURI(), null);
    }

    /**
     * Handle Validation Exceptions
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        log.warn("ValidationException: {}", e.getMessage());
        java.util.List<ErrorResponse.ValidationError> validationErrors = new java.util.ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.add(new ErrorResponse.ValidationError(fieldName, errorMessage));
        });

        return buildResponse(CommonErrorCode.INVALID_PARAMETER, "Validation Failed", request.getRequestURI(),
                validationErrors);
    }

    /**
     * Handle Bind Exceptions
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("BindException: {}", e.getMessage());
        return buildResponse(CommonErrorCode.INVALID_PARAMETER, "Bind Exception", request.getRequestURI(), null);
    }

    /**
     * Handle generic Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Internal Server Error: ", e);
        return buildResponse(CommonErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI(), null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(ErrorCode errorCode, String message, String path,
            Object details) {
        ErrorResponse.ErrorResponseBuilder builder = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(message != null ? message : errorCode.getMessage())
                .path(path)
                .timestamp(LocalDateTime.now());

        if (details instanceof java.util.List) {
            // Safe cast check or just assume if it matches signature
            try {
                @SuppressWarnings("unchecked")
                java.util.List<ErrorResponse.ValidationError> errors = (java.util.List<ErrorResponse.ValidationError>) details;
                if (!errors.isEmpty() && errors.get(0) instanceof ErrorResponse.ValidationError) {
                    builder.validationErrors(errors);
                } else {
                    builder.details(details);
                }
            } catch (Exception e) {
                builder.details(details);
            }
        } else {
            builder.details(details);
        }

        return ResponseEntity.status(errorCode.getHttpStatus()).body(builder.build());
    }
}
