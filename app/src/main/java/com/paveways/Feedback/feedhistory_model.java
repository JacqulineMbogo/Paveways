package com.paveways.Feedback;

public class feedhistory_model {

    private String comment, reply, commentdate, replydate;

    public feedhistory_model(String comment, String reply, String commentdate, String replydate) {
        this.comment = comment;
        this.reply = reply;
        this.commentdate = commentdate;
        this.replydate = replydate;
    }


    public String getComment() {
        return comment;
    }

    public String getReply() {
        return reply;
    }

    public String getCommentdate() {
        return commentdate;
    }

    public String getReplydate() {
        return replydate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setCommentdate(String commentdate) {
        this.commentdate = commentdate;
    }

    public void setReplydate(String replydate) {
        this.replydate = replydate;
    }
}
