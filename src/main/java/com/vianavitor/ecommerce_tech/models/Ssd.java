package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ssds")
public class Ssd extends Product {
    private String model;

    @Column(name = "capacity_gb")
    private short capacityGb;

    @Column(name = "interface")
    @Enumerated(EnumType.STRING)
    private SsdInterface ssdInterface = SsdInterface.SATA;

    @Column(name = "form_factor")
    @Enumerated(EnumType.STRING)
    private SsdFormFactor formFactor = SsdFormFactor.INCH_2_5;

    @Enumerated(EnumType.STRING)
    private SsdProtocol protocol = SsdProtocol.AHCI;

    @Column(name = "read_speed_mb")
    private short readSpeedMb;

    @Column(name = "write_speed_mb")
    private short writeSpeedMb;
}
