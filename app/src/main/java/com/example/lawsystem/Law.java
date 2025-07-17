package com.example.lawsystem;

public class Law {
    private String name;
    private String section;
    private String link;

    public Law(String name, String section,String link) {
        this.name = name;
        this.section = section;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }
    public String getLink() {
        return link;
    }
}

