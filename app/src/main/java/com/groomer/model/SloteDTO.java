package com.groomer.model;

import java.io.Serializable;

/**
 * Created by YusataInfotech on 8/7/2016.
 */
public class SloteDTO implements Serializable {

    private String start_time;
    private String end_time;
    private  String max_booking;
    private String id;
    private  String slot_id;
    private String selected = "";

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getMax_booking() {
        return max_booking;
    }

    public void setMax_booking(String max_booking) {
        this.max_booking = max_booking;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
