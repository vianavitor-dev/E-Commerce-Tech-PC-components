package com.vianavitor.ecommerce_tech.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.vianavitor.ecommerce_tech.exceptions.*;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<?> notFoundResourceException(NotFoundResourceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } 

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<?> duplicateUserException(DuplicateUserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeactivatedUserException.class)
    public ResponseEntity<?> deactivatedUserException(DeactivatedUserException e) {
        return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotCanceledException.class)
    public ResponseEntity<?> orderNoCanceledException(OrderNotCanceledException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailOrPasswordException.class)
    public ResponseEntity<?> invalidEmailOrPasswordException(InvalidEmailOrPasswordException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequiredTokenException.class)
    public ResponseEntity<?> missingRequiredTokenException(MissingRequiredTokenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<?> jwtVerificationException(JWTVerificationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> genericException(Exception e) {
        return new ResponseEntity<>("An error has occurred, please try again." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
