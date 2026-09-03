package com.vianavitor.ecommerce_tech.exceptions;

public class NotFoundResourceException extends RuntimeException {
    public NotFoundResourceException(String message) {
        super(message);
    }
}
