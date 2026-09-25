package com.vianavitor.ecommerce_tech.models.aux.enums;

public enum OrderStatus {
    IN_PROCESS,
    ON_ITS_WAY,
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
            case IN_PROCESS -> status = ON_ITS_WAY;
            case ON_ITS_WAY -> status = DELIVERED;
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
        return this.name().toLowerCase();
    }
}
