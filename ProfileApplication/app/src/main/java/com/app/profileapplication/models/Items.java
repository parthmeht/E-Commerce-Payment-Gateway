package com.app.profileapplication.models;

public class Items {

    String itemName, region, id, image;
    Double discount, price;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Items(String itemName, String region, String id, Double discount, Double price, String image) {
        this.itemName = itemName;
        this.region = region;
        this.id = id;
        this.discount = discount;
        this.price = price;
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
