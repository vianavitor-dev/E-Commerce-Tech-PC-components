package com.vianavitor.ecommerce_tech.dtos.request;

import jakarta.annotation.Nullable;

public record UserModifiableFieldsDTO(
        @Nullable String name,
        @Nullable String email
) {
}
