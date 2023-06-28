package com.paveways.WebResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class feedhistoryAPI {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Information")
    @Expose
    private List<Information> information = null;

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
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("reply")
        @Expose
        private String reply;
        @SerializedName("commentdate")
        @Expose
        private String commentdate;
        @SerializedName("replydate")
        @Expose
        private String replydate;
        @SerializedName("staff")
        @Expose
        private String staff;


        @SerializedName("id")
        @Expose
        private String id;

        public String getStaff() {
            return staff;
        }

        public void setStaff(String staff) {
            this.staff = staff;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getCommentdate() {
            return commentdate;
        }

        public void setCommentdate(String commentdate) {
            this.commentdate = commentdate;
        }

        public String getReplydate() {
            return replydate;
        }

        public void setReplydate(String replydate) {
            this.replydate = replydate;
        }

    }

}