package com.vianavitor.ecommerce_tech.auth.exceptions;

public class MissingRequiredTokenException extends RuntimeException {
    public MissingRequiredTokenException(String message) {
        super(message);
    }
}
