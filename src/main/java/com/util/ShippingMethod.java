package com.util;

public enum ShippingMethod {
    STANDARD(10.0),
    EXPRESS(20.0);

    private final double price;

    ShippingMethod(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }
}
