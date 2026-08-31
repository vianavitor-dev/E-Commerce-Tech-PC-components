package com.vianavitor.ecommerce_tech.models.aux.enums;

public enum MotherboardFormFactor {
    MINI_ITX("Mini-ITX"),
    MATX("mATX"),
    ATX("ATX"),
    E_ATX("E-ATX");

    private String formattedName;

    MotherboardFormFactor(String formattedName) {
        this.formattedName = formattedName;
    }

    public String getFormattedName() {
        return this.formattedName;
    }
}
