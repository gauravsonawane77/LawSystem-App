package com.example.lawsystem;

public class Criminal {
    private String fullName;
    private String description;
    private String photoId; // Assuming you have resource IDs for photos
    private String reward;

    public Criminal(String fullName, String description, String photoId, String reward) {
        this.fullName = fullName;
        this.description = description;
        this.photoId = photoId;
        this.reward = reward;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getReward() {
        return reward;
    }
}
