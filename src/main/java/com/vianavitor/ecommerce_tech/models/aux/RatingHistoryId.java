package com.vianavitor.ecommerce_tech.models.aux;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingHistoryId implements Serializable {
    private Integer userId;
    private Integer productId;
}