package com.huzhiying.server.exception;

public class AuthValidationException extends RuntimeException {

    public AuthValidationException(String message) {
        super(message);
    }
}
