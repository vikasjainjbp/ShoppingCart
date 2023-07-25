package com.capita.shopping.model;

public class PriceCatalog {

    String itemId;

    int price;

    Promotion promotion;

    public PriceCatalog(String itemId, int price, Promotion promotion) {
        this.itemId = itemId;
        this.price = price;
        this.promotion = promotion;
    }

    public PriceCatalog(String itemId, int price) {
        this.itemId = itemId;
        this.price = price;
        this.promotion = promotion;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "PriceCatalog{" +
                "itemId='" + itemId + '\'' +
                ", price=" + price +
                ", promotion=" + promotion +
                '}';
    }
}
