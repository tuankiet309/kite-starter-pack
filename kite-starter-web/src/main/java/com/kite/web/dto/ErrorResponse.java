package com.kite.web.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Object details;
    private java.util.List<ValidationError> validationErrors;

    @Data
    @Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}
