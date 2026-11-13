package com.vianavitor.ecommerce_tech.auth.dtos.request;

public record UserRegisterFormsDTO(
        String name, String email, String password
) {
}
