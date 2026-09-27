package com.vianavitor.ecommerce_tech.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record OrdersProductDTO (
        Integer id,
        String name,
        String brand,
        UserWhoBought user,
        ProductCategory category,
        BigDecimal price,
        Byte amount
) {
    public record UserWhoBought (
            Integer id,
            String name
    ) {}

    @JsonProperty
    public BigDecimal total() {
        var result = BigDecimal.valueOf(price.doubleValue() * amount);

        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
