package com.groomer.model;

import java.util.List;

/**
 * Created by Deepak Singh on 29-Mar-16.
 */
public class AppointmentDTO {

    private String date;
    private String address;
    private String time;
    private String order_id;
    private String storename_ara;
    private String store_id;
    private String storename_eng;
    private String image;
    private String status;
    private List<AppointServicesDTO> Service;
    private ReviewDTO review;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStorename_ara() {
        return storename_ara;
    }

    public void setStorename_ara(String storename_ara) {
        this.storename_ara = storename_ara;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStorename_eng() {
        return storename_eng;
    }

    public void setStorename_eng(String storename_eng) {
        this.storename_eng = storename_eng;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AppointServicesDTO> getService() {
        return Service;
    }

    public void setService(List<AppointServicesDTO> service) {
        Service = service;
    }

    public ReviewDTO getReview() {
        return review;
    }

    public void setReview(ReviewDTO review) {
        this.review = review;
    }
}
