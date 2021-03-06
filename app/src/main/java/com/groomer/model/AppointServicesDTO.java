package com.groomer.model;

import java.io.Serializable;

/**
 * Created by Deepak Singh on 27-Mar-16.
 */
public class AppointServicesDTO implements Serializable {
    private String service_id;
    private String service_name_eng;
    private String service_name_ara;
    private String price;
    private String duration;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name_eng() {
        return service_name_eng;
    }

    public void setService_name_eng(String service_name_eng) {
        this.service_name_eng = service_name_eng;
    }

    public String getService_name_ara() {
        return service_name_ara;
    }

    public void setService_name_ara(String service_name_ara) {
        this.service_name_ara = service_name_ara;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
