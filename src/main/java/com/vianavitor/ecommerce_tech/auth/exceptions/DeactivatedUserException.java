package com.vianavitor.ecommerce_tech.auth.exceptions;

public class DeactivatedUserException extends RuntimeException {
    public DeactivatedUserException(String message) {
        super(message);
    }
}
