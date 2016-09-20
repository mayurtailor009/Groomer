package com.groomer.model;


import java.util.List;

public class SaloonDetailsFullDTO {
    private SaloonDetailsDTO Saloon;
    private List<ServiceDTO> Service;
    private List<ReviewDTO> Review;
    private String message;
    private boolean status;

    public List<ReviewDTO> getReview() {
        return Review;
    }

    public void setReview(List<ReviewDTO> review) {
        Review = review;
    }

    public SaloonDetailsDTO getSaloon() {
        return Saloon;
    }

    public void setSaloon(SaloonDetailsDTO saloon) {
        Saloon = saloon;
    }

    public List<ServiceDTO> getService() {
        return Service;
    }

    public void setService(List<ServiceDTO> service) {
        Service = service;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
