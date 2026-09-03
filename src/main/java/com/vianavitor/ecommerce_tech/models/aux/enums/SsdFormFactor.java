package com.vianavitor.ecommerce_tech.models.aux.enums;

public enum SsdFormFactor {
    M2("M.2"),
    i2_5("2.5"+'"'),
    MSATA("mSATA"),
    U2("U.2");

    private String formattedName;

    SsdFormFactor(String formattedName) {
        this.formattedName = formattedName;
    }

    public String toString() {
        return formattedName;
    }
}
