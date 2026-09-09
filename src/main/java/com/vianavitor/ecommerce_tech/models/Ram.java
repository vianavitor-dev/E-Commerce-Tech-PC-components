package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.RamFormFactor;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "rams")
public class Ram extends Product {
    private String model;

    @Column(name = "capacity_gb")
    private byte capacityGb;

    @Column(name = "ddr_generation")
    private String ddrGeneration;

    @Column(name = "frequency_mhz")
    private short frequencyMhz;
    private byte modules;
    private RamFormFactor formFactor = RamFormFactor.DIMM;
}
