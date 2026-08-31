package com.vianavitor.ecommerce_tech.models;

import com.vianavitor.ecommerce_tech.models.aux.enums.PsuEfficiencyRating;
import com.vianavitor.ecommerce_tech.models.aux.enums.PsuFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.PsuModularity;
import jakarta.persistence.*;

@Entity(name = "power_supply_units")
public class Psu extends Product {
    private String model;

    @Column(name = "wattage_supply")
    private short wattageSupply;

    @Column(name = "efficiency_rating")
    @Enumerated(EnumType.STRING)
    private PsuEfficiencyRating efficiencyRating = PsuEfficiencyRating.STANDARD;

    @Enumerated(EnumType.STRING)
    private PsuModularity modularity;

    @Column(name = "form_factor")
    @Enumerated(EnumType.STRING)
    private PsuFormFactor formFactor = PsuFormFactor.ATX;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public short getWattageSupply() {
        return wattageSupply;
    }

    public void setWattageSupply(short wattageSupply) {
        this.wattageSupply = wattageSupply;
    }

    public PsuEfficiencyRating getEfficiencyRatting() {
        return efficiencyRating;
    }

    public void setEfficiencyRatting(PsuEfficiencyRating efficiencyRating) {
        this.efficiencyRating = efficiencyRating;
    }

    public PsuModularity getModularity() {
        return modularity;
    }

    public void setModularity(PsuModularity modularity) {
        this.modularity = modularity;
    }

    public PsuFormFactor getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(PsuFormFactor formFactor) {
        this.formFactor = formFactor;
    }
}
