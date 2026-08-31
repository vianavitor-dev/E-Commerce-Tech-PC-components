package com.vianavitor.ecommerce_tech.models.aux.enums;

public enum PsuFormFactor {
    ATX, SFX, SFX_L, TFX;

    public String toString() {
        return this.name().replace("_", "-");
    }
}
