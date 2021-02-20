package com.example.easydelivery.model;

public class Bussines {
    private String id;
    private String name;
    private String bussinesname;
    private String email;
    private String identification;
    private String type;
    private String token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBussinesname() {
        return bussinesname;
    }

    public void setBussinesname(String bussinesname) {
        this.bussinesname = bussinesname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
