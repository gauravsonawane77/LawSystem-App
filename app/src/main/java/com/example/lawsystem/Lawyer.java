package com.example.lawsystem;

public class Lawyer {
    private String name;
    private String expertise;
    private String court;
    private String contact;
    private String description;
    private String photo;
    private String id;

    public Lawyer(String name, String expertise, String court, String contact, String description,String photo,String id) {
        this.name = name;
        this.expertise = expertise;
        this.court = court;
        this.contact = contact;
        this.description = description;
        this.photo = photo;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getExpertise() {
        return expertise;
    }

    public String getCourt() {
        return court;
    }

    public String getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }
}

