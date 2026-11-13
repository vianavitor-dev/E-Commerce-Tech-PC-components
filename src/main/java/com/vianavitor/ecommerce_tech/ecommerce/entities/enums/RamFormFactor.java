package com.vianavitor.ecommerce_tech.ecommerce.entities.enums;

public enum RamFormFactor {
    DIMM,
    SO_DIMM;

    public String toString() {
        return this.name().replace("_", "-");
    }
}
