package com.huzhiying.server.exception;

public class ConfigNotFoundException extends RuntimeException {

    public ConfigNotFoundException(String message) {
        super(message);
    }
}
