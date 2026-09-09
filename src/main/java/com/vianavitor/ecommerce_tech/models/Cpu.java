package com.vianavitor.ecommerce_tech.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cpus")
public class Cpu extends Product {
    private String model;
    private String manufacturer;
    private String socket;
    private byte threads;
    private byte cores;

    @Column(name = "base_clock", precision = 3, scale = 1)
    private BigDecimal baseClock;

    @Column(name = "boost_clock", precision = 3, scale = 1)
    private BigDecimal boostClock;

    @Column(name = "tdp_watts")
    private short tdpWatts;
}
