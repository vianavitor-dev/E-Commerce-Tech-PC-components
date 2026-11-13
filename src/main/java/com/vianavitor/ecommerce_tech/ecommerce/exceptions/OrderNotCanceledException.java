package com.vianavitor.ecommerce_tech.ecommerce.exceptions;

public class OrderNotCanceledException extends RuntimeException {
    public OrderNotCanceledException(String message) {
        super(message);
    }
}
