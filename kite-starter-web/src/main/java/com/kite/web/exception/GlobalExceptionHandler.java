package com.kite.web.exception;

import com.kite.core.exception.BusinessException;
import com.kite.core.exception.CommonErrorCode;
import com.kite.core.exception.ErrorCode;
import com.kite.core.exception.KiteRuntimeException;
import com.kite.web.dto.ErrorResponse;
import com.kite.web.i18n.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageUtil messageUtil;

    // ------------------------------------------------------------------ //
    // Business Exceptions //
    // ------------------------------------------------------------------ //

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e,
            HttpServletRequest request) {
        log.warn("BusinessException [{}]: {}", e.getErrorCode().getCode(), e.getMessage());
        String message = resolveMessage(e.getErrorCode().getCode(), e.getArgs(), e.getMessage());
        return buildResponse(e.getErrorCode(), message, request.getRequestURI(), null);
    }

    // ------------------------------------------------------------------ //
    // Validation Exceptions //
    // ------------------------------------------------------------------ //

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        log.warn("ValidationException: {}", e.getMessage());
        List<ErrorResponse.ValidationError> validationErrors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            // Try to resolve field-level message via i18n, fall back to annotation message
            String msg = resolveMessage(error.getDefaultMessage(), null, error.getDefaultMessage());
            validationErrors.add(new ErrorResponse.ValidationError(field, msg));
        });

        String message = resolveMessage(CommonErrorCode.INVALID_PARAMETER.getCode(), null,
                CommonErrorCode.INVALID_PARAMETER.getMessage());
        return buildResponse(CommonErrorCode.INVALID_PARAMETER, message, request.getRequestURI(), validationErrors);
    }

    // ------------------------------------------------------------------ //
    // Generic Fallback //
    // ------------------------------------------------------------------ //

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled exception: ", e);
        String message = resolveMessage(CommonErrorCode.INTERNAL_SERVER_ERROR.getCode(), null,
                CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return buildResponse(CommonErrorCode.INTERNAL_SERVER_ERROR, message, request.getRequestURI(), null);
    }

    // ------------------------------------------------------------------ //
    // Helpers //
    // ------------------------------------------------------------------ //

    /**
     * Try to look up {@code key} in the message bundle.
     * If the key is not found (NoSuchMessageException) or key is null/blank,
     * fall back to {@code fallback} so the app never hard-crashes on a missing
     * translation.
     */
    private String resolveMessage(String key, Object[] args, String fallback) {
        if (key == null || key.isBlank()) {
            return fallback;
        }
        try {
            return messageUtil.getMessage(key, args, fallback);
        } catch (NoSuchMessageException ex) {
            log.debug("No i18n message for key '{}', using fallback.", key);
            return fallback;
        }
    }

    private ResponseEntity<ErrorResponse> buildResponse(ErrorCode errorCode,
            String message,
            String path,
            Object details) {
        ErrorResponse.ErrorResponseBuilder builder = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now());

        if (details instanceof List<?> list && !list.isEmpty()
                && list.get(0) instanceof ErrorResponse.ValidationError) {
            @SuppressWarnings("unchecked")
            List<ErrorResponse.ValidationError> errors = (List<ErrorResponse.ValidationError>) list;
            builder.validationErrors(errors);
        } else {
            builder.details(details);
        }

        return ResponseEntity.status(errorCode.getHttpStatus()).body(builder.build());
    }
}
