package com.smarttracker.product.exception;

import lombok.Getter;

@Getter
public class AuthenticationFailedException extends RuntimeException {
    
    private final String errorCode;
    
    public AuthenticationFailedException(String message) {
        super(message);
        this.errorCode = "AUTHENTICATION_FAILED";
    }
    
    public AuthenticationFailedException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "AUTHENTICATION_FAILED";
    }
}