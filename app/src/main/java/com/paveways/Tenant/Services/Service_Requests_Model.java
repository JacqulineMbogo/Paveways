package com.paveways.Tenant.Services;

public class Service_Requests_Model {

    private String requestid,service,status,user_comment,staff_comment,fullname, email,phone,amount,create_date,update_date ;

    public Service_Requests_Model(String requestid,String service, String status, String staff_comment,String user_comment, String fullname, String email, String phone, String amount, String create_date, String update_date) {
        this.requestid = requestid;
        this.service = service;
        this.status = status;
        this.staff_comment = staff_comment;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.user_comment = user_comment;
        this.amount=amount;
        this.create_date=create_date;
        this.update_date=update_date;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public String getStaff_comment() {
        return staff_comment;
    }

    public void setStaff_comment(String staff_comment) {
        this.staff_comment = staff_comment;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
