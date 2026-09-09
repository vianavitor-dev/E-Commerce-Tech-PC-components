package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.MotherboardFormFactor;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "motherboards")
public class Motherboard extends Product {
    private String model;
    private String chipset;
    private String socket;
    private String ddrGeneration;
    private byte ramSlots;
    private short maxRamCapacityGb;
    private byte sataSlots;
    private byte m2Slots;

    // TODO: create a new Entity to implements the motherboards ports

    private MotherboardFormFactor formFactor = MotherboardFormFactor.ATX;
}

