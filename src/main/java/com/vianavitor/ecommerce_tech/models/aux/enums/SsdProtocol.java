package com.vianavitor.ecommerce_tech.models.aux.enums;

public enum SsdProtocol {
    NVME("NVMe"),
    AHCI("AHCI");

    private String formattedName;

    SsdProtocol(String formattedName) {
        this.formattedName = formattedName;
    }

    public String getFormattedName() {
        return formattedName;
    }
}
