package com.vianavitor.ecommerce_tech.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public byte getVramGb() {
        return vramGb;
    }

    public void setVramGb(byte vramGb) {
        this.vramGb = vramGb;
    }

    public String getVramType() {
        return vramType;
    }

    public void setVramType(String vramType) {
        this.vramType = vramType;
    }

    public short getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(short powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public String getGpuInterface() {
        return gpuInterface;
    }

    public void setGpuInterface(String gpuInterface) {
        this.gpuInterface = gpuInterface;
    }

    public Short getRecommendedPsuWatts() {
        return recommendedPsuWatts;
    }

    public void setRecommendedPsuWatts(Short recommendedPsuWatts) {
        this.recommendedPsuWatts = recommendedPsuWatts;
    }
}
