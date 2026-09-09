package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String sku;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "rated_count")
    private int ratedCount;
    private String brand;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "technical_description")
    private String technicalDescription;

    @Column(precision = 8, scale = 2)
    private BigDecimal price;
    private short stock;
}

