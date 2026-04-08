package com.huzhiying.server.exception;

public class AuthUnauthorizedException extends RuntimeException {

    public AuthUnauthorizedException(String message) {
        super(message);
    }
}
