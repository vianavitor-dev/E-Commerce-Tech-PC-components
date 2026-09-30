package com.vianavitor.ecommerce_tech.exceptions;

public class MissingRequiredTokenException extends RuntimeException {
    public MissingRequiredTokenException(String message) {
        super(message);
    }
}
