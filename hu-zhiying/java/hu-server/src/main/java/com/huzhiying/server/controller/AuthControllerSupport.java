package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.exception.AuthUnauthorizedException;
import com.huzhiying.server.exception.AuthValidationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

public abstract class AuthControllerSupport {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("请求参数校验失败");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(message));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidation(HandlerMethodValidationException exception) {
        String message = exception.getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage() == null ? "请求参数校验失败" : error.getDefaultMessage())
                .orElse("请求参数校验失败");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("请求参数校验失败");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(message));
    }

    @ExceptionHandler(AuthValidationException.class)
    protected ResponseEntity<ApiResponse<Void>> handleAuthValidation(AuthValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(exception.getMessage()));
    }

    @ExceptionHandler(AuthUnauthorizedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleAuthUnauthorized(AuthUnauthorizedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail(exception.getMessage()));
    }
}
