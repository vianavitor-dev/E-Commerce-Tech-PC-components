package com.vianavitor.ecommerce_tech.ecommerce.dtos.response;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.ProductCategory;

import java.math.BigDecimal;

public interface ProductSearchResultsDTO {
    Integer getId();
    String getName();
    ProductCategory getCategory();
    BigDecimal getRating();
    int getRatedCount();
    String getBrand();
    BigDecimal getPrice();
}
