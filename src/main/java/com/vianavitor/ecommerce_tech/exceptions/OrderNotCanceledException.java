package com.vianavitor.ecommerce_tech.exceptions;

public class OrderNotCanceledException extends RuntimeException {
    public OrderNotCanceledException(String message) {
        super(message);
    }
}
