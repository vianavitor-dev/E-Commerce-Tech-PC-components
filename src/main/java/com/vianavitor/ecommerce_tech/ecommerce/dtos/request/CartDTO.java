package com.vianavitor.ecommerce_tech.ecommerce.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public record CartDTO(
        @NotNull(message = "Cart products are required")
        Map<@NotNull(message = "Cart product ID is required") Integer, Byte> products,
        @NotNull(message = "Cart last update is required") LocalDateTime lastUpdate) {
}
