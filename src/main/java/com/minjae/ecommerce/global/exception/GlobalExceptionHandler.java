package com.minjae.ecommerce.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    // @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidException(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 400,
                "errors", errors
        ));
    }

    // 비즈니스 예외
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: {}", e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", e.getErrorCode().getStatus().value(),
                "message", e.getErrorCode().getMessage()
        ));
    }

    // 그 외 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity.internalServerError().body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 500,
                "message", ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        ));
    }
}
