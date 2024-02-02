package com.padyak.dto;

public class Participants {
    private String userName,userImage;
    private String userId;
    public Participants(String userName, String userImage, String userId) {
        this.userName = userName;
        this.userImage = userImage;
        this.userId = userId;
    }

    public Participants() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


}
