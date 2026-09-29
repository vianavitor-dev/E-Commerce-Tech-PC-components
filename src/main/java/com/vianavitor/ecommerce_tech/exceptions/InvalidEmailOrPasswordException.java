package com.vianavitor.ecommerce_tech.exceptions;

public class InvalidEmailOrPasswordException extends RuntimeException {
    public InvalidEmailOrPasswordException(String msg) {
        super(msg);
    }

    public InvalidEmailOrPasswordException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
