package com.vianavitor.ecommerce_tech.dtos.response;

import com.vianavitor.ecommerce_tech.models.aux.enums.OrderStatus;

import java.time.LocalDate;

public record UsersOrderDTO(
        Integer id,
        OrderStatus status,
        LocalDate orderedAt,
        LocalDate statusUpdatedAt
) {
}
