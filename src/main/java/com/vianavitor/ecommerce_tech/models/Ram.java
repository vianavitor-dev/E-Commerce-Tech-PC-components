package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.RamFormFactor;
import jakarta.persistence.*;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public byte getCapacityGb() {
        return capacityGb;
    }

    public void setCapacityGb(byte capacityGb) {
        this.capacityGb = capacityGb;
    }

    public String getDdrGeneration() {
        return ddrGeneration;
    }

    public void setDdrGeneration(String ddrGeneration) {
        this.ddrGeneration = ddrGeneration;
    }

    public short getFrequencyMhz() {
        return frequencyMhz;
    }

    public void setFrequencyMhz(short frequencyMhz) {
        this.frequencyMhz = frequencyMhz;
    }

    public byte getModules() {
        return modules;
    }

    public void setModules(byte modules) {
        this.modules = modules;
    }

    public RamFormFactor getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(RamFormFactor formFactor) {
        this.formFactor = formFactor;
    }
}
