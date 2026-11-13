package com.vianavitor.ecommerce_tech.ecommerce.entities.enums;

public enum SsdProtocol {
    NVME("NVMe"),
    AHCI("AHCI");

    private String formattedName;

    SsdProtocol(String formattedName) {
        this.formattedName = formattedName;
    }

    public String toString() {
        return formattedName;
    }
}
