package com.padyak.dto;

public class Participants {
    private String userName,userImage;

    public Participants(String userName, String userImage) {
        this.userName = userName;
        this.userImage = userImage;
    }

    public Participants() {
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
