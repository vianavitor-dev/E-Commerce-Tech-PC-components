package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity(name = "ssds")
public class Ssd extends Product {
    private String model;

    @Column(name = "capacity_gb")
    private short capacityGb;

    @Column(name = "interface")
    private SsdInterface ssdInterface = SsdInterface.SATA;

    @Column(name = "form_factor")
    private SsdFormFactor formFactor = SsdFormFactor.INCH_2_5;

    private SsdProtocol protocol = SsdProtocol.AHCI;

    @Column(name = "read_speed_mb")
    private short readSpeedMb;

    @Column(name = "write_speed_mb")
    private short writeSpeedMb;

    public Ssd(String name, String sku, BigDecimal rating, int ratedCount, String brand, ProductCategory category, String shortDescription, String technicalDescription, BigDecimal price, short stock, String model, short capacityGb, SsdInterface ssdInterface, SsdFormFactor formFactor, SsdProtocol protocol, short readSpeedMb, short writeSpeedMb) {
        super(null, name, sku, rating, ratedCount, brand, category, shortDescription, technicalDescription, price, stock);
        this.model = model;
        this.capacityGb = capacityGb;
        this.ssdInterface = ssdInterface;
        this.formFactor = formFactor;
        this.protocol = protocol;
        this.readSpeedMb = readSpeedMb;
        this.writeSpeedMb = writeSpeedMb;
    }
}
