package com.vianavitor.ecommerce_tech.ecommerce.entities.enums;

public enum PsuEfficiencyRating {
    STANDARD,
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    TITANIUM;

    public String toString() {
        char firstCharCapitalized = this.name().toLowerCase().charAt(0);
        String restInLower = this.name().substring(1).toLowerCase();

        return firstCharCapitalized + restInLower;
    }
}
