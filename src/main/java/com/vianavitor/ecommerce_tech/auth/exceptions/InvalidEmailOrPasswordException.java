package com.vianavitor.ecommerce_tech.auth.exceptions;

public class InvalidEmailOrPasswordException extends RuntimeException {
    public InvalidEmailOrPasswordException(String msg) {
        super(msg);
    }

    public InvalidEmailOrPasswordException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
