package com.vianavitor.ecommerce_tech.models.aux.enums;

public enum PsuModularity {
    NON_MODULAR,
    SEMI_MODULAR,
    FULLY_MODULAR;

    public String getFormattedName() {
        return this.name().toLowerCase().replace("_", "-");
    }
}
