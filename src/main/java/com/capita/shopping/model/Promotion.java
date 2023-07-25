package com.capita.shopping.model;

public enum Promotion {

    BOGO("Buy One Get One"),
    THREEFORTWO("Three For Two");

    String promoName;

    Promotion(String promoName) {
        this.promoName = promoName;
    }
}