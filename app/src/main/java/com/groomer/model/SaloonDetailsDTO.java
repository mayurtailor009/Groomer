package com.groomer.model;


import java.util.List;

public class SaloonDetailsDTO {
    private String storename_eng;
    private String storename_ara;
    private String storedesc_eng;
    private String storedesc_ara;

    private String lat;
    private String lng;
    private String address;
    private String store_id;
    private String distance;
    private String rating;
    private String favourite;
    private String image;
    private List<ImagesDTO> images;

    public List<ImagesDTO> getImages() {
        return images;
    }

    public void setImages(List<ImagesDTO> images) {
        this.images = images;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
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

    public String getStoredesc_eng() {
        return storedesc_eng;
    }

    public void setStoredesc_eng(String storedesc_eng) {
        this.storedesc_eng = storedesc_eng;
    }

    public String getStoredesc_ara() {
        return storedesc_ara;
    }

    public void setStoredesc_ara(String storedesc_ara) {
        this.storedesc_ara = storedesc_ara;
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
