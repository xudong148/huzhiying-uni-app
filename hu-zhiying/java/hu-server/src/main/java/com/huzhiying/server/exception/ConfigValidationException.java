package com.huzhiying.server.exception;

public class ConfigValidationException extends RuntimeException {

    public ConfigValidationException(String message) {
        super(message);
    }
}
