package com.groomer.model;


import java.io.Serializable;

public class CategoryDTO implements Serializable {

    private String id;
    private String name_eng;
    private String name_ara;
    private String image;
    private String status;
    private String created;
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_eng() {
        return name_eng;
    }

    public void setName_eng(String name_eng) {
        this.name_eng = name_eng;
    }

    public String getName_ara() {
        return name_ara;
    }

    public void setName_ara(String name_ara) {
        this.name_ara = name_ara;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
