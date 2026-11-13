package com.vianavitor.ecommerce_tech.ecommerce.entities;

import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.MotherboardFormFactor;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity(name = "motherboards")
public class Motherboard extends Product {
    private String model;
    private String chipset;
    private String socket;
    private String ddrGeneration;
    private Byte ramSlots;
    private Short maxRamCapacityGb;
    private Byte sataSlots;
    private Byte m2Slots;

    // TODO: create a new Entity to implements the motherboards ports

    private MotherboardFormFactor formFactor = MotherboardFormFactor.ATX;
}

