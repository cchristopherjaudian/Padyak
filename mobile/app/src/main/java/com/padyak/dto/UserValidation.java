package com.padyak.dto;

import java.util.Objects;

public class UserValidation {
    private String userName, userImage, userId;
    private String paymentStatus;
    private String paymentURL;
    public UserValidation() {
    }

    public UserValidation(String userId, String userName, String userImage, String paymentStatus, String paymentURL) {
        this.userName = userName;
        this.userImage = userImage;
        this.userId = userId;
        this.paymentStatus = paymentStatus;
        this.paymentURL = paymentURL;
    }

    public String getPaymentURL() {
        return paymentURL;
    }

    public void setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserValidation that = (UserValidation) o;
        return Objects.equals(userName, that.userName) && Objects.equals(userImage, that.userImage) && Objects.equals(paymentStatus, that.paymentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userImage, paymentStatus);
    }

    @Override
    public String toString() {
        return "UserValidation{" +
                "userName='" + userName + '\'' +
                ", userImage='" + userImage + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
