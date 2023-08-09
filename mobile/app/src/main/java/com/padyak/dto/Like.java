package com.padyak.dto;

import java.util.Objects;

public class Like {
    private String userName,userImage,userId;

    public Like(String userName, String userImage, String userId) {
        this.userName = userName;
        this.userImage = userImage;
        this.userId = userId;
    }

    public Like() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(userName, like.userName) && Objects.equals(userImage, like.userImage) && Objects.equals(userId, like.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userImage, userId);
    }

    @Override
    public String toString() {
        return "Like{" +
                "userName='" + userName + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
