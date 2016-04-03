package com.groomer.model;

public class VendorListDTO {
    private String service_id;
    private String storename_eng;
    private String storename_ara;
    private String address;
    private String store_id;
    private String currency;
    private String currency_ara;
    private String price;
    private String distance;
    private String rating;
    private String favourite;
    private String image;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getStorename_eng() {
        return storename_eng;
    }

    public void setStorename_eng(String storename_eng) {
        this.storename_eng = storename_eng;
    }

    public String getStorename_ara() {
        return storename_ara;
    }

    public void setStorename_ara(String storename_ara) {
        this.storename_ara = storename_ara;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency_ara() {
        return currency_ara;
    }

    public void setCurrency_ara(String currency_ara) {
        this.currency_ara = currency_ara;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
