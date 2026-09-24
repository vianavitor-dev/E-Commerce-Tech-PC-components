package com.vianavitor.ecommerce_tech.models.aux.enums;

public enum OrderStatus {
    IN_PROGRESS,
    IN_TRANSIT,
    DELIVERED,
    CANCELED,
    REFUNDED;

    public OrderStatus refund() {
        if (this == OrderStatus.CANCELED)
            return OrderStatus.REFUNDED;

        return null;
    }

    public OrderStatus next() {
        OrderStatus status;

        switch(this) {
            case IN_PROGRESS -> status = IN_TRANSIT;
            case IN_TRANSIT -> status = DELIVERED;
            default -> status = this;
        }

        return status;
    }

    public OrderStatus next(boolean cancel) {
        if (cancel) return OrderStatus.CANCELED;

        return this.next();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase().replace("_", "-");
    }
}
