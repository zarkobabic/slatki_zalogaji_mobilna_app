package com.example.slatkizalogajimobilnaaplikacija.models;

import java.io.Serializable;

public class Comment implements Serializable {
    private String commentDescription;
    private String userName;

    public Comment() {
    }

    public Comment(String commentDescription, String userName) {
        this.commentDescription = commentDescription;
        this.userName = userName;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
