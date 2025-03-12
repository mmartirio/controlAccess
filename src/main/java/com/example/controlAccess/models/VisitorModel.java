package com.example.controlAccess.models;

import jakarta.persistence.Entity;

@Entity
public class VisitorModel extends UserModel {

    private String photo;

    public VisitorModel(Long id, String name, String surName, String rg, String phone, String photo) {
        super(id, name, surName, rg, phone);
        this.photo = photo;
    }

    public VisitorModel() {
        super();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
