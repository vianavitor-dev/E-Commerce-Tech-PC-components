package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.MotherboardFormFactor;
import jakarta.persistence.*;

@Entity(name = "motherboards")
public class Motherboard extends Product {
    private String model;
    private String chipset;
    private String socket;
    private String ddrGeneration;
    private byte ramSlots;
    private int maxRamCapacityGb; // TODO: change the data type in the database to 'smallint', and updates this field to matches it
    private byte sataSlots;
    private byte m2Slots;

    @Enumerated(EnumType.STRING)
    private MotherboardFormFactor formFactor = MotherboardFormFactor.ATX;

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

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getDdrGeneration() {
        return ddrGeneration;
    }

    public void setDdrGeneration(String ddrGeneration) {
        this.ddrGeneration = ddrGeneration;
    }

    public byte getRamSlots() {
        return ramSlots;
    }

    public void setRamSlots(byte ramSlots) {
        this.ramSlots = ramSlots;
    }

    public int getMaxRamCapacityGb() {
        return maxRamCapacityGb;
    }

    public void setMaxRamCapacityGb(int maxRamCapacityGb) {
        this.maxRamCapacityGb = maxRamCapacityGb;
    }

    public byte getSataSlots() {
        return sataSlots;
    }

    public void setSataSlots(byte sataSlots) {
        this.sataSlots = sataSlots;
    }

    public byte getM2Slots() {
        return m2Slots;
    }

    public void setM2Slots(byte m2Slots) {
        this.m2Slots = m2Slots;
    }

    public MotherboardFormFactor getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(MotherboardFormFactor formFactor) {
        this.formFactor = formFactor;
    }
}

