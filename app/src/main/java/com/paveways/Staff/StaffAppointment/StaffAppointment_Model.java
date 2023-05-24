package com.paveways.Staff.StaffAppointment;

public class StaffAppointment_Model {

    private String appointment_id, date, time, status,comment,title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StaffAppointment_Model(String appointment_id, String date, String time, String status, String comment, String title) {
        this.appointment_id = appointment_id;
        this.date = date;
        this.time = time;
        this.status = status;
        this.comment = comment;
        this.title = title;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
