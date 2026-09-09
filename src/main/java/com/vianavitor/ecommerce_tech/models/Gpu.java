package com.vianavitor.ecommerce_tech.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gpus")
public class Gpu extends Product {
    private String model;
    private String chipset;

    @Column(name = "vram_gb")
    private byte vramGb;

    @Column(name = "vram_type")
    private String vramType;

    @Column(name = "power_consumption")
    private short powerConsumption;

    @Column(name = "interface")
    private String gpuInterface;

    @Column(name = "recommended_psu_watts")
    private Short recommendedPsuWatts;
}
