package com.vianavitor.ecommerce_tech.ecommerce.entities.enums;

public enum PsuModularity {
    NON_MODULAR,
    SEMI_MODULAR,
    FULLY_MODULAR;

    public String toString() {
        return this.name().toLowerCase().replace("_", "-");
    }
}
