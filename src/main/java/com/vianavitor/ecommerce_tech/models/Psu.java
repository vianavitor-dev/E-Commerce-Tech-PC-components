package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.PsuEfficiencyRating;
import com.vianavitor.ecommerce_tech.models.aux.enums.PsuFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.PsuModularity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity(name = "power_supply_units")
public class Psu extends Product {
    private String model;

    @Column(name = "wattage_supply")
    private short wattageSupply;

    @Column(name = "efficiency_rating")
    private PsuEfficiencyRating efficiencyRating = PsuEfficiencyRating.STANDARD;
    private PsuModularity modularity;

    @Column(name = "form_factor")
    private PsuFormFactor formFactor = PsuFormFactor.ATX;
}
