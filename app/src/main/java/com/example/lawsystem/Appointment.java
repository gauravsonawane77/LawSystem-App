package com.example.lawsystem;

public class Appointment {
    public String id;
    public String userId;
    public String lawyerId;
    public String appointmentTime;
    public String status;
    public String appointmentType;
    public String lawyerName;
    public String lawyerEmail;
    public String lawyer_contact;

    public Appointment(String id, String userId, String lawyerId, String appointmentTime,
                       String status, String appointmentType, String lawyerName, String lawyerEmail, String lawyer_contact) {
        this.id = id;
        this.userId = userId;
        this.lawyerId = lawyerId;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.appointmentType = appointmentType;
        this.lawyerName = lawyerName;
        this.lawyerEmail = lawyerEmail;
        this.lawyer_contact = lawyer_contact;
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

    public String getLawyerName() {
        return lawyerName;
    }

    public String getLawyerEmail() {
        return lawyerEmail;
    }

    public String getLawyer_contact() {
        return lawyer_contact;
    }
}
