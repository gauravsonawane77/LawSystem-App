package com.example.lawsystem;

public class Message {
    public int senderId;
    public int receiverId;
    public String message;
    public String sentAt;

    public Message(int senderId, int receiverId, String message, String sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.sentAt = sentAt;
    }
}
