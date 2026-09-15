package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<?> notFoundResourceException(NotFoundResourceException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
