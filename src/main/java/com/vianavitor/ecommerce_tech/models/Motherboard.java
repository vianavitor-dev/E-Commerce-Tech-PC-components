package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.MotherboardFormFactor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    private MotherboardFormFactor formFactor = MotherboardFormFactor.ATX;
}

