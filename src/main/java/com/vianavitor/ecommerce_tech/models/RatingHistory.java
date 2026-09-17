package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.RatingHistoryId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "rating_history")
@IdClass(RatingHistoryId.class)
public class RatingHistory {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Column(precision = 2, scale = 1)
    private BigDecimal rate;
}
