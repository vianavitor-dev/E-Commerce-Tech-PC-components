package com.vianavitor.ecommerce_tech.exceptions;

public class DeactivatedUserException extends RuntimeException {
    public DeactivatedUserException(String message) {
        super(message);
    }
}
