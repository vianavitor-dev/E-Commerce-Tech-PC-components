package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
import jakarta.persistence.*;

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
    private SsdFormFactor formFactor = SsdFormFactor.i2_5;

    @Enumerated(EnumType.STRING)
    private SsdProtocol protocol = SsdProtocol.AHCI;

    @Column(name = "read_speed_mb")
    private short readSpeedMb;

    @Column(name = "write_speed_mb")
    private short writeSpeedMb;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public short getCapacityGb() {
        return capacityGb;
    }

    public void setCapacityGb(short capacityGb) {
        this.capacityGb = capacityGb;
    }

    public SsdInterface getSsdInterface() {
        return ssdInterface;
    }

    public void setSsdInterface(SsdInterface ssdInterface) {
        this.ssdInterface = ssdInterface;
    }

    public SsdFormFactor getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(SsdFormFactor formFactor) {
        this.formFactor = formFactor;
    }

    public SsdProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(SsdProtocol protocol) {
        this.protocol = protocol;
    }

    public short getReadSpeedMb() {
        return readSpeedMb;
    }

    public void setReadSpeedMb(short readSpeedMb) {
        this.readSpeedMb = readSpeedMb;
    }

    public short getWriteSpeedMb() {
        return writeSpeedMb;
    }

    public void setWriteSpeedMb(short writeSpeedMb) {
        this.writeSpeedMb = writeSpeedMb;
    }
}
