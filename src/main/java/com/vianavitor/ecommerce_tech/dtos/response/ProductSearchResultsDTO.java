package com.vianavitor.ecommerce_tech.dtos.response;

import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;

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
