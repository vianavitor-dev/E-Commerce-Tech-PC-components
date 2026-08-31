package com.vianavitor.ecommerce_tech.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public byte getThreads() {
        return threads;
    }

    public void setThreads(byte threads) {
        this.threads = threads;
    }

    public byte getCores() {
        return cores;
    }

    public void setCores(byte cores) {
        this.cores = cores;
    }

    public BigDecimal getBaseClock() {
        return baseClock;
    }

    public void setBaseClock(BigDecimal baseClock) {
        this.baseClock = baseClock;
    }

    public BigDecimal getBoostClock() {
        return boostClock;
    }

    public void setBoostClock(BigDecimal boostClock) {
        this.boostClock = boostClock;
    }

    public short getTdpWatts() {
        return tdpWatts;
    }

    public void setTdpWatts(short tdpWatts) {
        this.tdpWatts = tdpWatts;
    }
}
