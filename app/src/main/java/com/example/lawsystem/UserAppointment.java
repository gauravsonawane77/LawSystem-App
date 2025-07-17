package com.example.lawsystem;

public class UserAppointment {
    public String id;
    public String userId;
    public String lawyerId;
    public String appointmentTime;
    public String status;
    public String appointmentType;
    public String userName;
    public String userEmail;

    public UserAppointment(String id, String userId, String lawyerId, String appointmentTime,
                           String status, String appointmentType, String userName, String userEmail) {
        this.id = id;
        this.userId = userId;
        this.lawyerId = lawyerId;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.appointmentType = appointmentType;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getLawyerId() {
        return lawyerId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
