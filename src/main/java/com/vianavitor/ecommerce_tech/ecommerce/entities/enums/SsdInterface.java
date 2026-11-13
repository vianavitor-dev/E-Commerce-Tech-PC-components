package com.vianavitor.ecommerce_tech.ecommerce.entities.enums;

public enum SsdInterface {
    SATA("SATA"),
    PCIE("PCIe");

    private String formattedName;

    SsdInterface(String formattedName) {
        this.formattedName = formattedName;
    }

    public String toString() {
        return formattedName;
    }
}
