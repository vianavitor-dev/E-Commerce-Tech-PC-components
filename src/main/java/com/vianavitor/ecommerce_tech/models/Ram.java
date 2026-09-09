package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.RamFormFactor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    private RamFormFactor formFactor = RamFormFactor.DIMM;
}
