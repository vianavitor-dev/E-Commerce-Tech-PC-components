package com.vianavitor.ecommerce_tech.ecommerce.dtos.response;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.OrderStatus;

import java.time.LocalDate;

public record UsersOrderDTO(
        Integer id,
        OrderStatus status,
        LocalDate orderedAt,
        LocalDate statusUpdatedAt
) {
}
