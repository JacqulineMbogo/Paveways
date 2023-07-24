package com.paveways.WebResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceRequestsAPI {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Information")
    @Expose
    private List<Information> information;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Information> getInformation() {
        return information;
    }

    public void setInformation(List<Information> information) {
        this.information = information;
    }


    public class Information {

        @SerializedName("request_id")
        @Expose
        private String requestId;
        @SerializedName("service_id")
        @Expose
        private String serviceId;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("create_date")
        @Expose
        private String createDate;
        @SerializedName("update_date")
        @Expose
        private String updateDate;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("items")
        @Expose
        private String items;
        @SerializedName("staff_comments")
        @Expose
        private String staffComments;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("sno")
        @Expose
        private Integer sno;
        @SerializedName("service_name")
        @Expose
        private String serviceName;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("uemail")
        @Expose
        private String uemail;
        @SerializedName("uphone")
        @Expose
        private String uphone;
        @SerializedName("uid")
        @Expose
        private String uid;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getItems() {
            return items;
        }

        public void setItems(String items) {
            this.items = items;
        }

        public String getStaffComments() {
            return staffComments;
        }

        public void setStaffComments(String staffComments) {
            this.staffComments = staffComments;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Integer getSno() {
            return sno;
        }

        public void setSno(Integer sno) {
            this.sno = sno;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getUemail() {
            return uemail;
        }

        public void setUemail(String uemail) {
            this.uemail = uemail;
        }

        public String getUphone() {
            return uphone;
        }

        public void setUphone(String uphone) {
            this.uphone = uphone;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

    }
}
