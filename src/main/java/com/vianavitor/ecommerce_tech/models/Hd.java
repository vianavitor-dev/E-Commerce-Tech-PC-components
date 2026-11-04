package com.vianavitor.ecommerce_tech.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "hds")
public class Hd extends Product {
    private String model;
    private Short capacityGb;
    private Short readSpeedMb;
    private Short writeSpeedMb;
}
